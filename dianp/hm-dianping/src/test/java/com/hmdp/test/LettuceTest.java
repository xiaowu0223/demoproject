package com.hmdp.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.pojo.user;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

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
//    @Test
//    void testSaveUser(){
//        // 写入数据
//        user user = new user("wanda", 18);
//        redisTemplate.opsForValue().set("user",user);
//        // 获取数据
//        user user1 = (user) redisTemplate.opsForValue().get("user");
//        System.out.println("user1 = "+user1);
//    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    // Json工具
    private static final ObjectMapper mapper = new ObjectMapper();
    @Test
    void testSaveUser() throws JsonProcessingException {
        // 准备对象
        user user = new user("wendy", 12);
        // 手动序列化
        String json = mapper.writeValueAsString(user);
        stringRedisTemplate.opsForValue().set("user：2",json);
        // 读取数据
        String json1 = stringRedisTemplate.opsForValue().get("user：2");
        // 手动反序列化
        user user1 = mapper.readValue(json1, user.class);
        System.out.println("user1 = "+user1);
    }
}
