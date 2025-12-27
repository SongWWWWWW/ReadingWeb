package com.readingweb.service;

import com.readingweb.dto.LoginDTO;
import com.readingweb.dto.RegisterDTO;
import com.readingweb.dto.UserVO;
import com.readingweb.entity.User;

/**
 * 用户服务接口
 * 
 * @author ReadingWeb Team
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    UserVO register(RegisterDTO registerDTO);
    
    /**
     * 用户登录
     */
    String login(LoginDTO loginDTO);
    
    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);
    
    /**
     * 根据ID查询用户信息
     */
    UserVO getUserById(Long id);
}

