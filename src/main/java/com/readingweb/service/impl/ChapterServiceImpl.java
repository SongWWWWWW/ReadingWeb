package com.readingweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.readingweb.dto.ChapterDTO;
import com.readingweb.dto.ChapterVO;
import com.readingweb.entity.Book;
import com.readingweb.entity.Chapter;
import com.readingweb.mapper.BookMapper;
import com.readingweb.mapper.ChapterMapper;
import com.readingweb.service.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 章节服务实现类
 * 
 * @author ReadingWeb Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    
    private final ChapterMapper chapterMapper;
    private final BookMapper bookMapper;
    
    @Override
    @Transactional
    public ChapterVO createChapter(ChapterDTO chapterDTO) {
        // 验证书籍是否存在
        Book book = bookMapper.selectById(chapterDTO.getBookId());
        if (book == null) {
            throw new IllegalArgumentException("书籍不存在");
        }
        
        // 检查章节序号是否已存在
        Chapter existingChapter = chapterMapper.selectOne(
            new LambdaQueryWrapper<Chapter>()
                .eq(Chapter::getBookId, chapterDTO.getBookId())
                .eq(Chapter::getChapterNumber, chapterDTO.getChapterNumber())
        );
        
        if (existingChapter != null) {
            throw new IllegalArgumentException("该章节序号已存在");
        }
        
        // 创建章节
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(chapterDTO, chapter);
        chapter.setWordCount(calculateWordCount(chapterDTO.getContent()));
        chapter.setReadCount(0);
        chapter.setStatus(1);
        chapter.setIsFree(chapterDTO.getIsFree() != null ? chapterDTO.getIsFree() : 0);
        chapter.setCreateTime(LocalDateTime.now());
        chapter.setUpdateTime(LocalDateTime.now());
        
        chapterMapper.insert(chapter);
        
        // 更新书籍的章节数
        updateBookChapterCount(chapterDTO.getBookId());
        
        return convertToVO(chapter, true);
    }
    
    @Override
    @Transactional
    public ChapterVO updateChapter(Long id, ChapterDTO chapterDTO) {
        Chapter chapter = chapterMapper.selectById(id);
        if (chapter == null) {
            throw new IllegalArgumentException("章节不存在");
        }
        
        // 如果更新了章节序号，检查是否冲突
        if (chapterDTO.getChapterNumber() != null 
                && !chapterDTO.getChapterNumber().equals(chapter.getChapterNumber())) {
            Chapter existingChapter = chapterMapper.selectOne(
                new LambdaQueryWrapper<Chapter>()
                    .eq(Chapter::getBookId, chapter.getBookId())
                    .eq(Chapter::getChapterNumber, chapterDTO.getChapterNumber())
                    .ne(Chapter::getId, id)
            );
            
            if (existingChapter != null) {
                throw new IllegalArgumentException("该章节序号已存在");
            }
            chapter.setChapterNumber(chapterDTO.getChapterNumber());
        }
        
        // 更新其他字段
        if (StringUtils.hasText(chapterDTO.getTitle())) {
            chapter.setTitle(chapterDTO.getTitle());
        }
        if (StringUtils.hasText(chapterDTO.getContent())) {
            chapter.setContent(chapterDTO.getContent());
            chapter.setWordCount(calculateWordCount(chapterDTO.getContent()));
        }
        if (chapterDTO.getIsFree() != null) {
            chapter.setIsFree(chapterDTO.getIsFree());
        }
        
        chapter.setUpdateTime(LocalDateTime.now());
        chapterMapper.updateById(chapter);
        
        return convertToVO(chapter, true);
    }
    
    @Override
    public ChapterVO getChapterById(Long id) {
        Chapter chapter = chapterMapper.selectById(id);
        if (chapter == null) {
            return null;
        }
        
        // 增加阅读量
        incrementReadCount(id);
        
        return convertToVO(chapter, true);
    }
    
    @Override
    public Page<ChapterVO> getChaptersByBookId(Long bookId, Integer current, Integer size) {
        Page<Chapter> page = new Page<>(current, size);
        
        LambdaQueryWrapper<Chapter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Chapter::getBookId, bookId)
               .eq(Chapter::getStatus, 1)
               .orderByAsc(Chapter::getChapterNumber);
        
        Page<Chapter> chapterPage = chapterMapper.selectPage(page, wrapper);
        
        // 转换为VO（不包含内容）
        Page<ChapterVO> voPage = new Page<>(chapterPage.getCurrent(), chapterPage.getSize(), chapterPage.getTotal());
        List<ChapterVO> voList = chapterPage.getRecords().stream()
                .map(chapter -> convertToVO(chapter, false))
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    @Override
    @Transactional
    public void deleteChapter(Long id) {
        Chapter chapter = chapterMapper.selectById(id);
        if (chapter == null) {
            throw new IllegalArgumentException("章节不存在");
        }
        
        // 逻辑删除：将状态改为草稿
        chapter.setStatus(0);
        chapter.setUpdateTime(LocalDateTime.now());
        chapterMapper.updateById(chapter);
        
        // 更新书籍的章节数
        updateBookChapterCount(chapter.getBookId());
    }
    
    @Override
    @Transactional
    public void incrementReadCount(Long id) {
        Chapter chapter = chapterMapper.selectById(id);
        if (chapter != null) {
            chapter.setReadCount(chapter.getReadCount() + 1);
            chapterMapper.updateById(chapter);
        }
    }
    
    /**
     * 计算字数
     */
    private Integer calculateWordCount(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        // 简单计算：去除空白字符后的长度
        return content.replaceAll("\\s+", "").length();
    }
    
    /**
     * 更新书籍的章节数
     */
    private void updateBookChapterCount(Long bookId) {
        Long count = chapterMapper.selectCount(
            new LambdaQueryWrapper<Chapter>()
                .eq(Chapter::getBookId, bookId)
                .eq(Chapter::getStatus, 1)
        );
        
        Book book = bookMapper.selectById(bookId);
        if (book != null) {
            book.setChapterCount(count.intValue());
            bookMapper.updateById(book);
        }
    }
    
    /**
     * 转换为VO
     */
    private ChapterVO convertToVO(Chapter chapter, boolean includeContent) {
        ChapterVO vo = new ChapterVO();
        BeanUtils.copyProperties(chapter, vo);
        if (!includeContent) {
            vo.setContent(null); // 列表查询时不返回内容
        }
        return vo;
    }
}

