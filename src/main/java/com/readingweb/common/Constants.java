package com.readingweb.common;

/**
 * 常量类
 * 
 * @author ReadingWeb Team
 */
public class Constants {
    
    /**
     * 状态常量
     */
    public static class Status {
        /** 禁用/下架 */
        public static final Integer DISABLED = 0;
        /** 启用/上架 */
        public static final Integer ENABLED = 1;
    }
    
    /**
     * 完成状态常量
     */
    public static class Completed {
        /** 连载中 */
        public static final Integer SERIALIZING = 0;
        /** 已完结 */
        public static final Integer COMPLETED = 1;
    }
    
    /**
     * 默认值
     */
    public static class Default {
        /** 默认页码 */
        public static final Integer PAGE_NUM = 1;
        /** 默认每页大小 */
        public static final Integer PAGE_SIZE = 10;
        /** 最大每页大小 */
        public static final Integer MAX_PAGE_SIZE = 100;
    }
    
    /**
     * JWT相关
     */
    public static class Jwt {
        /** Token前缀 */
        public static final String TOKEN_PREFIX = "Bearer ";
        /** Token请求头 */
        public static final String TOKEN_HEADER = "Authorization";
    }
}

