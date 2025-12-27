package com.readingweb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 阅读记录实体类
 * 
 * @author ReadingWeb Team
 */
@Data
@TableName("reading_record")
public class ReadingRecord {
    
    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 书籍ID
     */
    private Long bookId;
    
    /**
     * 章节ID
     */
    private Long chapterId;
    
    /**
     * 阅读进度（百分比，0-100）
     */
    private Integer progress;
    
    /**
     * 最后阅读时间
     */
    private LocalDateTime lastReadTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

