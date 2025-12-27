package com.readingweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.readingweb.dto.BookVO;
import com.readingweb.entity.Book;
import com.readingweb.entity.Favorite;
import com.readingweb.mapper.BookMapper;
import com.readingweb.mapper.FavoriteMapper;
import com.readingweb.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类
 * 
 * @author ReadingWeb Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    
    private final FavoriteMapper favoriteMapper;
    private final BookMapper bookMapper;
    
    @Override
    @Transactional
    public void addFavorite(Long userId, Long bookId) {
        // 检查是否已收藏
        Favorite existing = favoriteMapper.selectOne(
            new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getBookId, bookId)
        );
        
        if (existing != null) {
            throw new IllegalArgumentException("已收藏该书籍");
        }
        
        // 验证书籍是否存在
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("书籍不存在");
        }
        
        // 添加收藏
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setBookId(bookId);
        favorite.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(favorite);
        
        // 更新书籍收藏数
        book.setFavoriteCount(book.getFavoriteCount() + 1);
        bookMapper.updateById(book);
    }
    
    @Override
    @Transactional
    public void removeFavorite(Long userId, Long bookId) {
        Favorite favorite = favoriteMapper.selectOne(
            new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getBookId, bookId)
        );
        
        if (favorite == null) {
            throw new IllegalArgumentException("未收藏该书籍");
        }
        
        // 删除收藏
        favoriteMapper.deleteById(favorite.getId());
        
        // 更新书籍收藏数
        Book book = bookMapper.selectById(bookId);
        if (book != null && book.getFavoriteCount() > 0) {
            book.setFavoriteCount(book.getFavoriteCount() - 1);
            bookMapper.updateById(book);
        }
    }
    
    @Override
    public boolean isFavorited(Long userId, Long bookId) {
        Favorite favorite = favoriteMapper.selectOne(
            new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getBookId, bookId)
        );
        return favorite != null;
    }
    
    @Override
    public Page<BookVO> getUserFavorites(Long userId, Integer current, Integer size) {
        Page<Favorite> page = new Page<>(current, size);
        
        // 查询用户的收藏记录
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .orderByDesc(Favorite::getCreateTime);
        
        Page<Favorite> favoritePage = favoriteMapper.selectPage(page, wrapper);
        
        // 获取书籍ID列表
        List<Long> bookIds = favoritePage.getRecords().stream()
                .map(Favorite::getBookId)
                .collect(Collectors.toList());
        
        if (bookIds.isEmpty()) {
            return new Page<>(current, size, 0);
        }
        
        // 查询书籍信息
        List<Book> books = bookMapper.selectBatchIds(bookIds);
        
        // 转换为VO
        List<BookVO> voList = books.stream()
                .map(book -> {
                    BookVO vo = new BookVO();
                    BeanUtils.copyProperties(book, vo);
                    return vo;
                })
                .collect(Collectors.toList());
        
        Page<BookVO> voPage = new Page<>(favoritePage.getCurrent(), favoritePage.getSize(), favoritePage.getTotal());
        voPage.setRecords(voList);
        
        return voPage;
    }
}

