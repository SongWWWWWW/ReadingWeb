package com.readingweb.dto;

import lombok.Data;

/**
 * 登录请求DTO
 * 
 * @author ReadingWeb Team
 */
@Data
public class LoginDTO {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
}

