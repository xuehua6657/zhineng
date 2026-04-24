package com.blog.annotation;

import java.lang.annotation.*;

/**
 * 接口限流注解
 * 使用方式: @RateLimit(seconds = 60, maxCount = 10)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /** 时间窗口(秒) */
    int seconds() default 60;

    /** 最大请求次数 */
    int maxCount() default 10;

    /** 限流提示信息 */
    String message() default "请求过于频繁，请稍后重试";
}
