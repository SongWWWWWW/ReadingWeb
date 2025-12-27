package com.readingweb.controller;

import com.readingweb.common.Result;
import com.readingweb.dto.LoginDTO;
import com.readingweb.dto.RegisterDTO;
import com.readingweb.dto.UserVO;
import com.readingweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 
 * @author ReadingWeb Team
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody RegisterDTO registerDTO) {
        UserVO userVO = userService.register(registerDTO);
        return Result.success("注册成功", userVO);
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        String token = userService.login(loginDTO);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return Result.success("登录成功", data);
    }
}

