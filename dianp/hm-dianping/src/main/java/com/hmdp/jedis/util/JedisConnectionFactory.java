package com.hmdp.jedis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

// 定义连接池工具类
public class JedisConnectionFactory {
    // 静态成员变量
    private static final JedisPool jedisPool;
    static {
        // 配置连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8); // 最大连接数
        poolConfig.setMaxIdle(8); // 最大空闲数
        poolConfig.setMinIdle(0); // 最小空闲数
        poolConfig.setMaxWait(Duration.ofMillis(1000)); // 最大等待时间
        // 创建连接池对象
        jedisPool = new JedisPool(poolConfig,
                "192.168.30.128", 6379,
                1000, "123456");
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
