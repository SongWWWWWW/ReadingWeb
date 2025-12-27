package com.readingweb.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.readingweb.dto.ChapterDTO;
import com.readingweb.dto.ChapterVO;

import java.util.List;

/**
 * 章节服务接口
 * 
 * @author ReadingWeb Team
 */
public interface ChapterService {
    
    /**
     * 创建章节
     */
    ChapterVO createChapter(ChapterDTO chapterDTO);
    
    /**
     * 更新章节
     */
    ChapterVO updateChapter(Long id, ChapterDTO chapterDTO);
    
    /**
     * 根据ID查询章节（包含内容）
     */
    ChapterVO getChapterById(Long id);
    
    /**
     * 根据书籍ID查询章节列表（不包含内容）
     */
    Page<ChapterVO> getChaptersByBookId(Long bookId, Integer current, Integer size);
    
    /**
     * 删除章节
     */
    void deleteChapter(Long id);
    
    /**
     * 增加章节阅读量
     */
    void incrementReadCount(Long id);
}

