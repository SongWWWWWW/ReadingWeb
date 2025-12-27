package com.readingweb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏实体类
 * 
 * @author ReadingWeb Team
 */
@Data
@TableName("favorite")
public class Favorite {
    
    /**
     * 收藏ID
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
     * 创建时间
     */
    private LocalDateTime createTime;
}

