package com.readingweb.dto;

import lombok.Data;

/**
 * 书籍查询DTO
 * 
 * @author ReadingWeb Team
 */
@Data
public class BookQueryDTO {
    
    /**
     * 书名（模糊查询）
     */
    private String title;
    
    /**
     * 作者（模糊查询）
     */
    private String author;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 标签
     */
    private String tag;
    
    /**
     * 是否完结：0-连载中，1-已完结
     */
    private Integer isCompleted;
    
    /**
     * 状态：0-下架，1-上架
     */
    private Integer status;
    
    /**
     * 排序字段：readCount-阅读量，favoriteCount-收藏数，rating-评分，createTime-创建时间
     */
    private String orderBy;
    
    /**
     * 排序方式：asc-升序，desc-降序
     */
    private String orderDirection;
    
    /**
     * 当前页
     */
    private Integer current = 1;
    
    /**
     * 每页大小
     */
    private Integer size = 10;
}

