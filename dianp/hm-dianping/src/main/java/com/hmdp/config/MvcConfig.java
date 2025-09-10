package com.hmdp.config;

import com.hmdp.utils.LoginInterceptor;
import com.hmdp.utils.RefreshTokeninterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置需要拦截的路径
        registry.addInterceptor(new LoginInterceptor())
                // 配置放行路径（不需要拦截）
                .excludePathPatterns(
                        "/user/code",
                        "/user/login",
                        "/shop/**",
                        "/voucher/**",
                        "/blog/hot",
                        "/upload/**",
                        "/shop-type/**"
                // order配置拦截器优先级
                ).order(1);
        // 刷新所有请求
        registry.addInterceptor(new RefreshTokeninterceptor(stringRedisTemplate))
                .addPathPatterns("/**").order(0);
    }
}
