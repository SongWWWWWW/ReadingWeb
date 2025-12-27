package com.readingweb.dto;

import lombok.Data;

/**
 * 用户视图对象
 * 
 * @author ReadingWeb Team
 */
@Data
public class UserVO {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 用户状态
     */
    private Integer status;
}

