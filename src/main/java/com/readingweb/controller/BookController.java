package com.readingweb.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.readingweb.common.Result;
import com.readingweb.dto.BookDTO;
import com.readingweb.dto.BookQueryDTO;
import com.readingweb.dto.BookVO;
import com.readingweb.service.BookService;
import com.readingweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 书籍控制器
 * 
 * @author ReadingWeb Team
 */
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    
    private final BookService bookService;
    private final UserService userService;
    
    /**
     * 创建书籍
     */
    @PostMapping
    public Result<BookVO> createBook(@Valid @RequestBody BookDTO bookDTO, HttpServletRequest request) {
        // 获取当前用户信息，自动设置作者为当前用户的昵称
        Long userId = (Long) request.getAttribute("userId");
        if (userId != null) {
            var userVO = userService.getUserById(userId);
            if (userVO != null && userVO.getNickname() != null) {
                bookDTO.setAuthor(userVO.getNickname());
            }
        }
        BookVO bookVO = bookService.createBook(bookDTO);
        return Result.success("创建成功", bookVO);
    }
    
    /**
     * 更新书籍
     */
    @PutMapping("/{id}")
    public Result<BookVO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        BookVO bookVO = bookService.updateBook(id, bookDTO);
        return Result.success("更新成功", bookVO);
    }
    
    /**
     * 根据ID查询书籍
     */
    @GetMapping("/{id}")
    public Result<BookVO> getBookById(@PathVariable Long id) {
        BookVO bookVO = bookService.getBookById(id);
        if (bookVO == null) {
            return Result.error("书籍不存在");
        }
        return Result.success(bookVO);
    }
    
    /**
     * 分页查询书籍
     */
    @GetMapping("/list")
    public Result<Page<BookVO>> queryBooks(BookQueryDTO queryDTO) {
        Page<BookVO> page = bookService.queryBooks(queryDTO);
        return Result.success(page);
    }
    
    /**
     * 删除书籍
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return Result.success("删除成功");
    }
    
    /**
     * 查询我的书籍
     */
    @GetMapping("/my")
    public Result<Page<BookVO>> getMyBooks(
            BookQueryDTO queryDTO,
            HttpServletRequest request) {
        // 获取当前用户信息
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "未登录");
        }
        
        var userVO = userService.getUserById(userId);
        if (userVO == null) {
            return Result.error("用户不存在");
        }
        
        // 使用用户昵称作为作者名查询
        String author = userVO.getNickname() != null ? userVO.getNickname() : userVO.getUsername();
        Page<BookVO> page = bookService.queryMyBooks(author, queryDTO);
        return Result.success(page);
    }
}

