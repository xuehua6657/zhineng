package com.blog.common.constant;

/**
 * Redis Key 常量
 */
public class RedisConstants {

    /** 文章缓存前缀 */
    public static final String ARTICLE_KEY = "article:";
    /** 文章点赞前缀 */
    public static final String ARTICLE_LIKE_KEY = "article:like:";
    /** 接口限流前缀 */
    public static final String RATE_LIMIT_KEY = "rate:limit:";
    /** 用户信息缓存 */
    public static final String USER_KEY = "user:";
}
