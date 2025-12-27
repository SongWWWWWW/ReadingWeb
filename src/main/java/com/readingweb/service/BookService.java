package com.readingweb.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.readingweb.dto.BookDTO;
import com.readingweb.dto.BookQueryDTO;
import com.readingweb.dto.BookVO;

/**
 * 书籍服务接口
 * 
 * @author ReadingWeb Team
 */
public interface BookService {
    
    /**
     * 创建书籍
     */
    BookVO createBook(BookDTO bookDTO);
    
    /**
     * 更新书籍
     */
    BookVO updateBook(Long id, BookDTO bookDTO);
    
    /**
     * 根据ID查询书籍
     */
    BookVO getBookById(Long id);
    
    /**
     * 分页查询书籍
     */
    Page<BookVO> queryBooks(BookQueryDTO queryDTO);
    
    /**
     * 删除书籍（逻辑删除）
     */
    void deleteBook(Long id);
    
    /**
     * 增加阅读量
     */
    void incrementReadCount(Long id);
    
    /**
     * 查询我的书籍（根据作者名称）
     */
    Page<BookVO> queryMyBooks(String author, BookQueryDTO queryDTO);
}

