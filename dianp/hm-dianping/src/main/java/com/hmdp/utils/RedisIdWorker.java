package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 描述：全局唯一ID生成器
 * @Date: 2025/09/13 14:35
 * @Description:通过拼接时间戳和序列号生成全局唯一ID，在递增的同时保证安全性；
 * 理论上可用时间戳长达60+年

 */
@Component
public class RedisIdWorker {
    /**
     * 序列号位数
     */
    private static final long COUNT_BITS = 32;
    /**
    * 开始时间戳
    */
    private static final long BEGIN_TIMESTANP = 1735689600;
    private StringRedisTemplate redisTemplate;
    public long nextId(String keyPrefix){
        //生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = nowSecond - BEGIN_TIMESTANP;
        //生成序列号
        //获取当前日期
        String date = now.format(DateTimeFormatter.ofPattern("yyyy：MM：dd"));
        long sequence = redisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);
        //拼接并返回
        return timeStamp << COUNT_BITS | sequence;
    }

//    // 打印开始时间戳
//    public static void main(String[] args){
//        LocalDateTime time = LocalDateTime.of(2025,1,1,0,0,0);
//        long second = time.toEpochSecond(ZoneOffset.UTC);
//        System.out.println(second);
//    }
}
