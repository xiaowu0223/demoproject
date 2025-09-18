package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;

public class RedissionConfig {
    @Bean
    public RedissonClient redissionClient(){
        //配置项
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.30.130:6379")
                .setPassword("123456");
        return  Redisson.create(config);
    }
}
