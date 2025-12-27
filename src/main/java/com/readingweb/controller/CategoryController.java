package com.readingweb.controller;

import com.readingweb.common.Result;
import com.readingweb.entity.Category;
import com.readingweb.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类控制器
 * 
 * @author ReadingWeb Team
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryMapper categoryMapper;
    
    /**
     * 获取所有启用的分类
     */
    @GetMapping("/list")
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = categoryMapper.selectAllEnabled();
        return Result.success(categories);
    }
}

