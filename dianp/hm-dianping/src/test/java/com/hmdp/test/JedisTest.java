package com.hmdp.test;

import com.hmdp.jedis.util.JedisConnectionFactory;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class JedisTest {
    private Jedis jedis;

    @BeforeEach //junit5注解，用于test前初始化
    void setUp(){
        // 建立连接，设置连接配置
//        jedis=new Jedis("192.168.30.128",6379);  单个redis实例
        jedis = JedisConnectionFactory.getJedis();
//        jedis.auth("123456");
        // 选择库
        jedis.select(0);
    }

    @Test
    // 字符串测试
    void testString(){
        // 存入数据
        String result = jedis.set("name","wanda");
        System.out.println("result = "+result);
        // 获取数据
        String name = jedis.get("name");
        System.out.println("name = "+name);
    }

    @Test
    // 哈希测试
    void testHash(){
        // 插入单个值
        jedis.hset("user:1","name","wilson");
        jedis.hset("user:1","age","unknow");

        // 获取值，这里会自动将多个值转换为map类型
        Map<String,String> map = jedis.hgetAll("user:1");
        System.out.println(map);
    }
    @AfterEach
    void tearDown(){
        if(jedis != null){
            jedis.close();
        }
    }

}
