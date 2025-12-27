package com.readingweb.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 书籍视图对象
 * 
 * @author ReadingWeb Team
 */
@Data
public class BookVO {
    
    /**
     * 书籍ID
     */
    private Long id;
    
    /**
     * 书名
     */
    private String title;
    
    /**
     * 作者
     */
    private String author;
    
    /**
     * 简介
     */
    private String description;
    
    /**
     * 封面图片URL
     */
    private String coverImage;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 标签
     */
    private String tags;
    
    /**
     * 总字数
     */
    private Integer wordCount;
    
    /**
     * 章节数
     */
    private Integer chapterCount;
    
    /**
     * 阅读量
     */
    private Integer readCount;
    
    /**
     * 收藏数
     */
    private Integer favoriteCount;
    
    /**
     * 评分
     */
    private BigDecimal rating;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 是否完结
     */
    private Integer isCompleted;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

