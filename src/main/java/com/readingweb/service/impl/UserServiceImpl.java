package com.readingweb.service.impl;

import com.readingweb.dto.LoginDTO;
import com.readingweb.dto.RegisterDTO;
import com.readingweb.dto.UserVO;
import com.readingweb.entity.User;
import com.readingweb.mapper.UserMapper;
import com.readingweb.service.UserService;
import com.readingweb.util.JwtUtil;
import com.readingweb.util.PasswordUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 * 
 * @author ReadingWeb Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    
    @Override
    public UserVO register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, registerDTO.getUsername())
        );
        
        if (existingUser != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (registerDTO.getEmail() != null && !registerDTO.getEmail().isEmpty()) {
            User existingEmail = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, registerDTO.getEmail())
            );
            
            if (existingEmail != null) {
                throw new IllegalArgumentException("邮箱已被注册");
            }
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(PasswordUtil.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        userMapper.insert(user);
        
        // 转换为VO返回
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
    
    @Override
    public String login(LoginDTO loginDTO) {
        // 查询用户
        User user = getByUsername(loginDTO.getUsername());
        
        if (user == null) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        
        // 验证密码
        if (!PasswordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new IllegalArgumentException("用户已被禁用");
        }
        
        // 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        
        return jwtUtil.generateToken(claims);
    }
    
    @Override
    public User getByUsername(String username) {
        return userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
        );
    }
    
    @Override
    public UserVO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return null;
        }
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}

