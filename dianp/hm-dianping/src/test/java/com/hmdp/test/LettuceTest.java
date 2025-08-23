package com.hmdp.test;

import com.hmdp.pojo.user;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class LettuceTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testSpring(){
        // 存入
        redisTemplate.opsForValue().set("name","wanda");
        // 获取
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }
    @Test
    void testSaveUser(){
        user user = new user("wanda", 18);
        redisTemplate.opsForValue().set("user",user);
    }
}
