package com.readingweb.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.readingweb.common.Result;
import com.readingweb.dto.BookVO;
import com.readingweb.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器
 * 
 * @author ReadingWeb Team
 */
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    /**
     * 添加收藏
     */
    @PostMapping("/{bookId}")
    public Result<?> addFavorite(HttpServletRequest request, @PathVariable Long bookId) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.addFavorite(userId, bookId);
        return Result.success("收藏成功");
    }
    
    /**
     * 取消收藏
     */
    @DeleteMapping("/{bookId}")
    public Result<?> removeFavorite(HttpServletRequest request, @PathVariable Long bookId) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.removeFavorite(userId, bookId);
        return Result.success("取消收藏成功");
    }
    
    /**
     * 检查是否已收藏
     */
    @GetMapping("/check/{bookId}")
    public Result<Boolean> checkFavorite(HttpServletRequest request, @PathVariable Long bookId) {
        Long userId = (Long) request.getAttribute("userId");
        boolean isFavorited = favoriteService.isFavorited(userId, bookId);
        return Result.success(isFavorited);
    }
    
    /**
     * 获取用户的收藏列表
     */
    @GetMapping("/list")
    public Result<Page<BookVO>> getUserFavorites(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = (Long) request.getAttribute("userId");
        Page<BookVO> page = favoriteService.getUserFavorites(userId, current, size);
        return Result.success(page);
    }
}

