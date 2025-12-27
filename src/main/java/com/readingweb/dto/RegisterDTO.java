package com.readingweb.dto;

import lombok.Data;

/**
 * 注册请求DTO
 * 
 * @author ReadingWeb Team
 */
@Data
public class RegisterDTO {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 昵称
     */
    private String nickname;
}

