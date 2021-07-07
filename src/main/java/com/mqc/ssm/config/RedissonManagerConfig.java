package com.mqc.ssm.config;

import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

/**
 * @author  maoqichuan
 * @date 2021年07月01日 9:50
 */
@Configuration
public class RedissonManagerConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.urls:121.4.124.95}")
    private String urls;

    /**
     * 本地环境就默认的单机模式
     * @author maoqichuan
     * @date 2021/7/1 10:04
     * @return org.redisson.api.RedissonClient
     */
    @Bean(name = "redissonClient")
    public RedissonClient redissonClientSingle() throws IOException {
        Config config = new Config();
        //单节点
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        if (StrUtil.isEmpty(password)) {
            config.useSingleServer().setPassword(null);
        } else {
            config.useSingleServer().setPassword(password);
        }
        RedissonClient redisson = Redisson.create(config);
        // 可通过打印redisson.getConfig().toJSON().toString()来检测是否配置成功
        System.out.println(redisson.getConfig().toJSON().toString());
        return redisson;
    }

//    @Profile("pro")
//    @Bean(name = "redissonClient")
//    public RedissonClient redissonClientCluster() throws IOException {
//        String[] nodes = urls.split(",");
//        // redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
//        for (int i = 0; i < nodes.length; i++) {
//            nodes[i] = "redis://" + nodes[i];
//        }
//        RedissonClient redisson = null;
//        Config config = new Config();
//        config.useClusterServers() // 这是用的集群server
//                .setScanInterval(2000) // 设置集群状态扫描时间
//                .addNodeAddress(nodes).setPassword(password);
//        redisson = Redisson.create(config);
//        // 可通过打印redisson.getConfig().toJSON().toString()来检测是否配置成功
//        return redisson;
//    }
}
