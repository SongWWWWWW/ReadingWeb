-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密后）',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT DEFAULT 1 COMMENT '用户状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID（0表示顶级分类）',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 书籍表
CREATE TABLE IF NOT EXISTS `book` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '书籍ID',
    `title` VARCHAR(200) NOT NULL COMMENT '书名',
    `author` VARCHAR(100) NOT NULL COMMENT '作者',
    `description` TEXT COMMENT '简介',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片URL',
    `category_id` BIGINT NOT NULL COMMENT '分类ID',
    `category_name` VARCHAR(50) DEFAULT NULL COMMENT '分类名称（冗余字段）',
    `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签（多个用逗号分隔）',
    `word_count` INT DEFAULT 0 COMMENT '总字数',
    `chapter_count` INT DEFAULT 0 COMMENT '章节数',
    `read_count` INT DEFAULT 0 COMMENT '阅读量',
    `favorite_count` INT DEFAULT 0 COMMENT '收藏数',
    `rating` DECIMAL(3,1) DEFAULT 0.0 COMMENT '评分（0-10分）',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `is_completed` TINYINT DEFAULT 0 COMMENT '是否完结：0-连载中，1-已完结',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_title` (`title`),
    KEY `idx_author` (`author`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表';

-- 章节表
CREATE TABLE IF NOT EXISTS `chapter` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节ID',
    `book_id` BIGINT NOT NULL COMMENT '书籍ID',
    `title` VARCHAR(200) NOT NULL COMMENT '章节标题',
    `chapter_number` INT NOT NULL COMMENT '章节序号',
    `content` LONGTEXT COMMENT '章节内容',
    `word_count` INT DEFAULT 0 COMMENT '字数',
    `is_free` TINYINT DEFAULT 0 COMMENT '是否免费：0-收费，1-免费',
    `read_count` INT DEFAULT 0 COMMENT '阅读量',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-草稿，1-已发布',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_book_id` (`book_id`),
    KEY `idx_chapter_number` (`book_id`, `chapter_number`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='章节表';

-- 阅读记录表
CREATE TABLE IF NOT EXISTS `reading_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `book_id` BIGINT NOT NULL COMMENT '书籍ID',
    `chapter_id` BIGINT DEFAULT NULL COMMENT '章节ID',
    `progress` INT DEFAULT 0 COMMENT '阅读进度（百分比，0-100）',
    `last_read_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后阅读时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_book` (`user_id`, `book_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_book_id` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='阅读记录表';

-- 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `book_id` BIGINT NOT NULL COMMENT '书籍ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_book` (`user_id`, `book_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_book_id` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 插入默认分类数据
INSERT INTO `category` (`name`, `description`, `parent_id`, `sort_order`, `status`) VALUES
('玄幻', '玄幻小说分类', 0, 1, 1),
('都市', '都市小说分类', 0, 2, 1),
('历史', '历史小说分类', 0, 3, 1),
('军事', '军事小说分类', 0, 4, 1),
('游戏', '游戏小说分类', 0, 5, 1),
('科幻', '科幻小说分类', 0, 6, 1),
('武侠', '武侠小说分类', 0, 7, 1),
('言情', '言情小说分类', 0, 8, 1);
