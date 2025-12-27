package com.readingweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.readingweb.dto.BookDTO;
import com.readingweb.dto.BookQueryDTO;
import com.readingweb.dto.BookVO;
import com.readingweb.entity.Book;
import com.readingweb.entity.Category;
import com.readingweb.mapper.BookMapper;
import com.readingweb.mapper.CategoryMapper;
import com.readingweb.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 书籍服务实现类
 * 
 * @author ReadingWeb Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    
    private final BookMapper bookMapper;
    private final CategoryMapper categoryMapper;
    
    @Override
    @Transactional
    public BookVO createBook(BookDTO bookDTO) {
        // 验证分类是否存在
        Category category = categoryMapper.selectById(bookDTO.getCategoryId());
        if (category == null) {
            throw new IllegalArgumentException("分类不存在");
        }
        
        // 创建书籍
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        book.setCategoryName(category.getName());
        book.setReadCount(0);
        book.setFavoriteCount(0);
        book.setChapterCount(0);
        book.setRating(BigDecimal.ZERO);
        book.setStatus(1);
        book.setIsCompleted(bookDTO.getIsCompleted() != null ? bookDTO.getIsCompleted() : 0);
        book.setCreateTime(LocalDateTime.now());
        book.setUpdateTime(LocalDateTime.now());
        
        bookMapper.insert(book);
        
        return convertToVO(book);
    }
    
    @Override
    @Transactional
    public BookVO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new IllegalArgumentException("书籍不存在");
        }
        
        // 如果更新了分类，验证分类是否存在
        if (bookDTO.getCategoryId() != null && !bookDTO.getCategoryId().equals(book.getCategoryId())) {
            Category category = categoryMapper.selectById(bookDTO.getCategoryId());
            if (category == null) {
                throw new IllegalArgumentException("分类不存在");
            }
            book.setCategoryId(category.getId());
            book.setCategoryName(category.getName());
        }
        
        // 更新其他字段
        if (StringUtils.hasText(bookDTO.getTitle())) {
            book.setTitle(bookDTO.getTitle());
        }
        if (StringUtils.hasText(bookDTO.getAuthor())) {
            book.setAuthor(bookDTO.getAuthor());
        }
        if (bookDTO.getDescription() != null) {
            book.setDescription(bookDTO.getDescription());
        }
        if (bookDTO.getCoverImage() != null) {
            book.setCoverImage(bookDTO.getCoverImage());
        }
        if (bookDTO.getTags() != null) {
            book.setTags(bookDTO.getTags());
        }
        if (bookDTO.getWordCount() != null) {
            book.setWordCount(bookDTO.getWordCount());
        }
        if (bookDTO.getIsCompleted() != null) {
            book.setIsCompleted(bookDTO.getIsCompleted());
        }
        
        book.setUpdateTime(LocalDateTime.now());
        bookMapper.updateById(book);
        
        return convertToVO(book);
    }
    
    @Override
    public BookVO getBookById(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            return null;
        }
        
        // 增加阅读量
        incrementReadCount(id);
        
        return convertToVO(book);
    }
    
    @Override
    public Page<BookVO> queryBooks(BookQueryDTO queryDTO) {
        Page<Book> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        
        // 书名模糊查询
        if (StringUtils.hasText(queryDTO.getTitle())) {
            wrapper.like(Book::getTitle, queryDTO.getTitle());
        }
        
        // 作者模糊查询
        if (StringUtils.hasText(queryDTO.getAuthor())) {
            wrapper.like(Book::getAuthor, queryDTO.getAuthor());
        }
        
        // 分类查询
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(Book::getCategoryId, queryDTO.getCategoryId());
        }
        
        // 标签查询
        if (StringUtils.hasText(queryDTO.getTag())) {
            wrapper.like(Book::getTags, queryDTO.getTag());
        }
        
        // 是否完结
        if (queryDTO.getIsCompleted() != null) {
            wrapper.eq(Book::getIsCompleted, queryDTO.getIsCompleted());
        }
        
        // 状态查询
        if (queryDTO.getStatus() != null) {
            wrapper.eq(Book::getStatus, queryDTO.getStatus());
        } else {
            // 默认只查询上架的书籍
            wrapper.eq(Book::getStatus, 1);
        }
        
        // 排序
        if (StringUtils.hasText(queryDTO.getOrderBy())) {
            String orderBy = queryDTO.getOrderBy();
            boolean isAsc = "asc".equalsIgnoreCase(queryDTO.getOrderDirection());
            
            switch (orderBy) {
                case "readCount":
                    wrapper.orderBy(true, isAsc, Book::getReadCount);
                    break;
                case "favoriteCount":
                    wrapper.orderBy(true, isAsc, Book::getFavoriteCount);
                    break;
                case "rating":
                    wrapper.orderBy(true, isAsc, Book::getRating);
                    break;
                case "createTime":
                default:
                    wrapper.orderBy(true, isAsc, Book::getCreateTime);
                    break;
            }
        } else {
            // 默认按创建时间倒序
            wrapper.orderByDesc(Book::getCreateTime);
        }
        
        Page<Book> bookPage = bookMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<BookVO> voPage = new Page<>(bookPage.getCurrent(), bookPage.getSize(), bookPage.getTotal());
        List<BookVO> voList = bookPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new IllegalArgumentException("书籍不存在");
        }
        
        // 逻辑删除：将状态改为下架
        book.setStatus(0);
        book.setUpdateTime(LocalDateTime.now());
        bookMapper.updateById(book);
    }
    
    @Override
    @Transactional
    public void incrementReadCount(Long id) {
        Book book = bookMapper.selectById(id);
        if (book != null) {
            book.setReadCount(book.getReadCount() + 1);
            bookMapper.updateById(book);
        }
    }
    
    @Override
    public Page<BookVO> queryMyBooks(String author, BookQueryDTO queryDTO) {
        Page<Book> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        
        // 根据作者名称查询
        if (StringUtils.hasText(author)) {
            wrapper.eq(Book::getAuthor, author);
        }
        
        // 书名模糊查询
        if (StringUtils.hasText(queryDTO.getTitle())) {
            wrapper.like(Book::getTitle, queryDTO.getTitle());
        }
        
        // 分类查询
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(Book::getCategoryId, queryDTO.getCategoryId());
        }
        
        // 是否完结
        if (queryDTO.getIsCompleted() != null) {
            wrapper.eq(Book::getIsCompleted, queryDTO.getIsCompleted());
        }
        
        // 状态查询（我的书籍不限制状态，可以查看所有状态）
        if (queryDTO.getStatus() != null) {
            wrapper.eq(Book::getStatus, queryDTO.getStatus());
        }
        
        // 排序
        if (StringUtils.hasText(queryDTO.getOrderBy())) {
            String orderBy = queryDTO.getOrderBy();
            boolean isAsc = "asc".equalsIgnoreCase(queryDTO.getOrderDirection());
            
            switch (orderBy) {
                case "readCount":
                    wrapper.orderBy(true, isAsc, Book::getReadCount);
                    break;
                case "favoriteCount":
                    wrapper.orderBy(true, isAsc, Book::getFavoriteCount);
                    break;
                case "rating":
                    wrapper.orderBy(true, isAsc, Book::getRating);
                    break;
                case "updateTime":
                    wrapper.orderBy(true, isAsc, Book::getUpdateTime);
                    break;
                case "createTime":
                default:
                    wrapper.orderBy(true, isAsc, Book::getCreateTime);
                    break;
            }
        } else {
            // 默认按更新时间倒序
            wrapper.orderByDesc(Book::getUpdateTime);
        }
        
        Page<Book> bookPage = bookMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<BookVO> voPage = new Page<>(bookPage.getCurrent(), bookPage.getSize(), bookPage.getTotal());
        List<BookVO> voList = bookPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    /**
     * 转换为VO
     */
    private BookVO convertToVO(Book book) {
        BookVO vo = new BookVO();
        BeanUtils.copyProperties(book, vo);
        return vo;
    }
}

