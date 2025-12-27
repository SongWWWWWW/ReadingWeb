package com.readingweb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 书籍实体类
 * 
 * @author ReadingWeb Team
 */
@Data
@TableName("book")
public class Book {
    
    /**
     * 书籍ID
     */
    @TableId(type = IdType.AUTO)
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
     * 分类名称（冗余字段，便于查询）
     */
    private String categoryName;
    
    /**
     * 标签（多个用逗号分隔）
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
     * 评分（0-10分）
     */
    private BigDecimal rating;
    
    /**
     * 状态：0-下架，1-上架
     */
    private Integer status;
    
    /**
     * 是否完结：0-连载中，1-已完结
     */
    private Integer isCompleted;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

