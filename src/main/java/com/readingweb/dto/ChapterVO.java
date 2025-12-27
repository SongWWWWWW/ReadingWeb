package com.readingweb.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节视图对象
 * 
 * @author ReadingWeb Team
 */
@Data
public class ChapterVO {
    
    /**
     * 章节ID
     */
    private Long id;
    
    /**
     * 书籍ID
     */
    private Long bookId;
    
    /**
     * 章节标题
     */
    private String title;
    
    /**
     * 章节序号
     */
    private Integer chapterNumber;
    
    /**
     * 章节内容（列表查询时不返回，详情查询时返回）
     */
    private String content;
    
    /**
     * 字数
     */
    private Integer wordCount;
    
    /**
     * 是否免费
     */
    private Integer isFree;
    
    /**
     * 阅读量
     */
    private Integer readCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

