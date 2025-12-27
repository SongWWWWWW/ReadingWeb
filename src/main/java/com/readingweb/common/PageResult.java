package com.readingweb.common;

import lombok.Data;

import java.util.List;

/**
 * 分页结果封装类
 * 
 * @author ReadingWeb Team
 */
@Data
public class PageResult<T> {
    
    /**
     * 当前页
     */
    private Long current;
    
    /**
     * 每页大小
     */
    private Long size;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 总页数
     */
    private Long pages;
    
    /**
     * 数据列表
     */
    private List<T> records;
    
    public PageResult() {
    }
    
    public PageResult(Long current, Long size, Long total, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.pages = (total + size - 1) / size; // 计算总页数
        this.records = records;
    }
    
    /**
     * 从MyBatis Plus的Page对象创建
     */
    public static <T> PageResult<T> of(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        return new PageResult<>(
            page.getCurrent(),
            page.getSize(),
            page.getTotal(),
            page.getRecords()
        );
    }
}

