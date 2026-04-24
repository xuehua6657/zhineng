-- H2 内存数据库初始化脚本
-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `nickname` VARCHAR(50),
    `email` VARCHAR(100),
    `avatar` VARCHAR(255),
    `status` TINYINT DEFAULT 1,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `icon` VARCHAR(50),
    `sort` INT DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `article` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(100) NOT NULL,
    `summary` VARCHAR(300),
    `content` CLOB NOT NULL,
    `cover_image` VARCHAR(255),
    `category_id` BIGINT,
    `author_id` BIGINT NOT NULL,
    `view_count` INT DEFAULT 0,
    `like_count` INT DEFAULT 0,
    `comment_count` INT DEFAULT 0,
    `status` TINYINT DEFAULT 1,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `content` VARCHAR(500) NOT NULL,
    `article_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `parent_id` BIGINT DEFAULT 0,
    `reply_user_id` BIGINT,
    `like_count` INT DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `article_like` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `article_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_username ON sys_user(username);
CREATE UNIQUE INDEX IF NOT EXISTS uk_article_user ON article_like(article_id, user_id);

-- 插入初始数据
INSERT INTO category (name, icon, sort) VALUES
('Java', 'icon-java', 1),
('前端', 'icon-html5', 2),
('数据库', 'icon-database', 3),
('架构', 'icon-server', 4),
('工具', 'icon-tools', 5);

-- 密码: 123456 (BCrypt)
INSERT INTO sys_user (username, password, nickname, email) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin@blog.com'),
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '测试用户', 'test@blog.com');
