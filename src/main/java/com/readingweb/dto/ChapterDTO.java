package com.readingweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 章节创建/更新DTO
 * 
 * @author ReadingWeb Team
 */
@Data
public class ChapterDTO {
    
    /**
     * 书籍ID
     */
    @NotNull(message = "书籍ID不能为空")
    private Long bookId;
    
    /**
     * 章节标题
     */
    @NotBlank(message = "章节标题不能为空")
    private String title;
    
    /**
     * 章节序号
     */
    @NotNull(message = "章节序号不能为空")
    private Integer chapterNumber;
    
    /**
     * 章节内容
     */
    @NotBlank(message = "章节内容不能为空")
    private String content;
    
    /**
     * 是否免费
     */
    private Integer isFree;
}

