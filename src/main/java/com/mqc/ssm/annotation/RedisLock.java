package com.mqc.ssm.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis进行上锁
 * @author maoqichuan
 * @date 2021/6/30 17:32
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
    /**
     * redis锁 key 名字
     */
    String lockName() default "";

    /**
     * redis锁 key 支持SPEL表达式
     */
    String key() default "";

    /**
     * 过期秒数,默认为5毫秒
     * @return 轮询锁的时间
     */
    int expire() default 5000;

    /**
     * 超时时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 获取锁失败异常提示
     */
    String message() default "获取分布式锁失败，请稍后重试!";
}
