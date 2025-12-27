package com.readingweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 书籍创建/更新DTO
 * 
 * @author ReadingWeb Team
 */
@Data
public class BookDTO {
    
    /**
     * 书名
     */
    @NotBlank(message = "书名不能为空")
    private String title;
    
    /**
     * 作者
     */
    @NotBlank(message = "作者不能为空")
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
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    /**
     * 标签
     */
    private String tags;
    
    /**
     * 总字数
     */
    private Integer wordCount;
    
    /**
     * 是否完结
     */
    private Integer isCompleted;
}

