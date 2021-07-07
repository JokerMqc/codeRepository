package com.mqc.ssm.aspect;

import cn.hutool.core.util.StrUtil;
import com.mqc.ssm.annotation.RedisLock;
import com.mqc.ssm.util.SpelUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * redisLock切面
 * @author maoqichuan
 * @date 2021年06月30日 17:39
 */
@Aspect
@Component
public class RedisLockAspect {
    @Autowired
    private RedissonClient redissonClient;
    private static final String REDISSON_LOCK_PREFIX = "redisson_lock:";

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable{
        String spel = redisLock.key();
        String lockName = redisLock.lockName();

        RLock rlock = redissonClient.getLock(getRedisKey(joinPoint, spel, lockName));
        boolean tryLock = rlock.tryLock(redisLock.expire(), redisLock.timeUnit());

        if (!tryLock){
            throw new Exception(redisLock.message());
        }

        Object result = null;
        try {
            //执行方法
            result = joinPoint.proceed();
        }finally {
            rlock.unlock();
        }

        return result;
    }

    /**
     * 将spel转换为字符串
     * @author maoqichuan
     * @date 2021/6/30 18:04
     * @param joinPoint 切点
     * @param lockName 锁名字
     * @param spel 表达式
     * @return java.lang.String
     */
    private String getRedisKey(ProceedingJoinPoint joinPoint,String spel,String lockName){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        return REDISSON_LOCK_PREFIX + lockName + StrUtil.COLON + SpelUtil.parse(target,spel,targetMethod,args);
    }
}
