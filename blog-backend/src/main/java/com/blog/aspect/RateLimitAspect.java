package com.blog.aspect;

import com.blog.annotation.RateLimit;
import com.blog.common.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * 限流切面 - Redis 不可用时自动降级
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint point, RateLimit rateLimit) throws Throwable {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();

            String ip = request.getRemoteAddr();
            String key = "rate:limit:" + ip + ":" + request.getRequestURI();

            Long count = redisTemplate.opsForValue().increment(key);
            if (count != null && count == 1) {
                redisTemplate.expire(key, rateLimit.seconds(), TimeUnit.SECONDS);
            }

            if (count != null && count > rateLimit.maxCount()) {
                throw new BizException(429, rateLimit.message());
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            // Redis 不可用时降级: 直接放行
            log.debug("限流降级: {}", e.getMessage());
        }

        return point.proceed();
    }
}
