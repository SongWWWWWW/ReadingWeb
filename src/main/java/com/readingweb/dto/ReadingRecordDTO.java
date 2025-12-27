package com.readingweb.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 阅读记录DTO
 * 
 * @author ReadingWeb Team
 */
@Data
public class ReadingRecordDTO {
    
    /**
     * 书籍ID
     */
    @NotNull(message = "书籍ID不能为空")
    private Long bookId;
    
    /**
     * 章节ID
     */
    @NotNull(message = "章节ID不能为空")
    private Long chapterId;
    
    /**
     * 阅读进度（0-100）
     */
    private Integer progress;
}

