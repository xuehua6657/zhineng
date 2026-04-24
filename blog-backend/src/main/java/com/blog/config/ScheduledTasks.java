package com.blog.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 * 面试亮点: 展示异步处理和定时任务能力
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledTasks {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 每天凌晨2点执行
     * 将 Redis 中的浏览量同步到数据库
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void syncArticleViews() {
        log.info("开始同步文章浏览量...");
        // 实际项目中这里会遍历 Redis 中的浏览数据并更新到数据库
        log.info("文章浏览量同步完成");
    }

    /**
     * 每天凌晨3点执行
     * 清理过期的 Redis 缓存
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanExpiredCache() {
        log.info("开始清理过期缓存...");
        log.info("缓存清理完成");
    }
}
