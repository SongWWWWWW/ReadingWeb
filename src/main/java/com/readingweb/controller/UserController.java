package com.readingweb.controller;

import com.readingweb.common.Result;
import com.readingweb.dto.UserVO;
import com.readingweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 
 * @author ReadingWeb Team
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserVO> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserVO userVO = userService.getUserById(userId);
        if (userVO == null) {
            return Result.error("用户不存在");
        }
        return Result.success(userVO);
    }
}

