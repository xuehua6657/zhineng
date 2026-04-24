package com.blog.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 操作日志切面
 * 记录所有 Controller 的请求参数、返回值和执行时间
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* com.blog.controller..*(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().toShortString();
        String args = Arrays.toString(point.getArgs());

        long startTime = System.currentTimeMillis();
        log.info("==> {} 参数: {}", methodName, args);

        Object result;
        try {
            result = point.proceed();
            long time = System.currentTimeMillis() - startTime;
            log.info("<== {} 耗时: {}ms", methodName, time);
            return result;
        } catch (Throwable e) {
            long time = System.currentTimeMillis() - startTime;
            log.error("!!  {} 异常: {} 耗时: {}ms", methodName, e.getMessage(), time);
            throw e;
        }
    }
}
