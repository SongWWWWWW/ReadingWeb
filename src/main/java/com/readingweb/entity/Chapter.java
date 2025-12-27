package com.readingweb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节实体类
 * 
 * @author ReadingWeb Team
 */
@Data
@TableName("chapter")
public class Chapter {
    
    /**
     * 章节ID
     */
    @TableId(type = IdType.AUTO)
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
     * 章节内容
     */
    private String content;
    
    /**
     * 字数
     */
    private Integer wordCount;
    
    /**
     * 是否免费：0-收费，1-免费
     */
    private Integer isFree;
    
    /**
     * 阅读量
     */
    private Integer readCount;
    
    /**
     * 状态：0-草稿，1-已发布
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

