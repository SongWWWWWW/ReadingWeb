package com.readingweb.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.readingweb.dto.BookVO;

/**
 * 收藏服务接口
 * 
 * @author ReadingWeb Team
 */
public interface FavoriteService {
    
    /**
     * 添加收藏
     */
    void addFavorite(Long userId, Long bookId);
    
    /**
     * 取消收藏
     */
    void removeFavorite(Long userId, Long bookId);
    
    /**
     * 检查是否已收藏
     */
    boolean isFavorited(Long userId, Long bookId);
    
    /**
     * 获取用户的收藏列表
     */
    Page<BookVO> getUserFavorites(Long userId, Integer current, Integer size);
}

