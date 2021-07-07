package com.mqc.ssm.util;

import com.mqc.ssm.common.lock.DistributedLocker;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁工具类
 * @author maoqichuan
 * @date 2021年07月01日 15:37
 */
public class RedisLockUtil {
    private static DistributedLocker distributedLocker = SpringContextHolder.getBean(DistributedLocker.class);

    /**
     * 加锁
     * @author maoqichuan
     * @date 2021/7/1 16:40
     * @param lockKey
     */
    public static RLock lock(String lockKey) {
        return distributedLocker.lock(lockKey);
    }

    /**
     * 释放锁
     * @author maoqichuan
     * @date 2021/7/1 16:41
     * @param lockKey 锁名字
     */
    public static void unlock(String lockKey) {
        distributedLocker.unlock(lockKey);
    }

    /**
     * 释放锁
     * @author maoqichuan
     * @date 2021/7/1 16:57
     * @param lock
     */
    public static void unlock(RLock lock) {
        distributedLocker.unlock(lock);
    }

    /**
     * 带超时的锁
     * @author maoqichuan
     * @date 2021/7/1 16:58
     * @param lockKey 锁名字
     * @param timeout 超时时间   单位：秒
     * @return org.redisson.api.RLock
     */
    public static RLock lock(String lockKey, int timeout) {
        return distributedLocker.lock(lockKey, timeout);
    }

    /**
     * 带超时的锁
     * @author maoqichuan
     * @date 2021/7/1 16:58
     * @param lockKey  锁名字
     * @param timeout 超时时间   单位：秒
     * @param unit 时间单位
     */
    public static RLock lock(String lockKey, int timeout, TimeUnit unit) {
        return distributedLocker.lock(lockKey, unit, timeout);
    }

    /**
     * 尝试获取锁
     * @author maoqichuan
     * @date 2021/7/1 16:59
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        return distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
    }

    /**
     * 尝试获取锁
     * @author maoqichuan
     * @date 2021/7/1 16:59
     * @param lockKey
     * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     */
    public static boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        return distributedLocker.tryLock(lockKey, unit, waitTime, leaseTime);
    }
}
