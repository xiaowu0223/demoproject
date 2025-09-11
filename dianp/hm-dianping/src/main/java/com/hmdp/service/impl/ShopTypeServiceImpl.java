package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryTypeList() {
        String cacheKey = "cache:shop:type";
        // 从redis查询商铺类型
        List<String> shopTypeJsonList =stringRedisTemplate.opsForList().range("cache:shop:type", 0, -1);
        // 存在且不为空，返回
        if (!CollectionUtil.isEmpty(shopTypeJsonList)) {
            try {
                // 将JSON字符串转换为ShopType对象列表
                List<ShopType> shopTypeList = shopTypeJsonList.stream()
                        .map(json -> JSONUtil.toBean(json, ShopType.class))
                        .collect(Collectors.toList());
                return Result.ok(shopTypeList);
            } catch (Exception e) {
                // JSON解析失败，清除缓存重新查询
                stringRedisTemplate.delete(cacheKey);
            }
        }
        // 不存在，查询数据库
        List<ShopType> typeList = query().orderByAsc("sort").list();
        // 数据库不存在，返回错误
        if (CollectionUtil.isEmpty(typeList)){
            return Result.fail("店铺类型不存在");
        }
        // 存在，将对象列表转换为JSON字符串列表并写入redis
        List<String> jsonList = typeList.stream()
                .map(JSONUtil::toJsonStr)
                .collect(Collectors.toList());

        stringRedisTemplate.opsForList().rightPushAll(cacheKey, jsonList);

//        // 设置过期时间，防止数据一直不更新
//        stringRedisTemplate.expire(cacheKey, 1, TimeUnit.HOURS);
        // 返回店铺类型
        return Result.ok(typeList);

    }
}
