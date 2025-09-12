package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.CacheClient;
import com.hmdp.utils.RedisData;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private CacheClient cacheClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryById(Long id) {
        //缓存穿透
//        queryWithPassThrough(id);
//        //互斥锁解决缓存击穿
//        Shop shop = cacheClient.queryWithPassThrough(
//                CACHE_SHOP_KEY, id, Shop.class,this::getById,
//                CACHE_NULL_TTL, TimeUnit.MINUTES);
        //逻辑过期解决缓存击穿
        Shop shop = cacheClient.queryWithLogicExpire(
                CACHE_SHOP_KEY,  Shop.class,id,
                this::getById, CACHE_NULL_TTL, TimeUnit.MINUTES);
        if (shop == null){
            return Result.fail("店铺不存在");
        }

        //返回商铺信息
        return Result.ok(shop);
    }
    //互斥锁 缓存击穿
//    public Shop queryWithMutex(Long id) {
//        String key = CACHE_SHOP_KEY + id;
//        String lockKey = null;
//        Shop shop = null;
//        try {
//            // 从redis查询商铺缓存
//            String shopJson = stringRedisTemplate.opsForValue().get(key);
//            //判断商户是否存在
//            if (StrUtil.isNotBlank(shopJson)){
//                // 存在则返回
//                return JSONUtil.toBean(shopJson, Shop.class);
//            }
//            //命中的是否是空值
//            if (shopJson != null){
//                return null;
//            }
//            // 缓存重建
//            //获取互斥锁
//            lockKey = LOCK_SHOP_KEY + id;
//            boolean isLock = tryLock(lockKey);
//            //判断是否获取成功
//            if (!isLock){
//                //失败，休眠并重试
//                Thread.sleep(50);
//                return queryWithMutex(id);
//            }
//            //成功，根据id查询数据库
//            // 不存在则查询数据库
//            shop = getById( id);
//            // 不存在返回错误
//            if (shop == null){
//                //将空值写入redis
//                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
//                //返回错误信息
//                return null;
//            }
//            //存在，写入redis
//            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }finally {
//            //释放锁
//            unLock(lockKey);
//        }
//        //返回商铺信息
//        return shop;
//    }

//    //线程池
//    private static final ExecutorService CACHE_THREAD_POOL = Executors.newFixedThreadPool(10);

    //缓存逻辑 缓存击穿
//    public Shop queryWithLogicExpire(Long id) {
//        String key = CACHE_SHOP_KEY + id;
//        // 从redis查询商铺缓存
//        String shopJson = stringRedisTemplate.opsForValue().get(key);
//        //判断商户是否存在
//        if (StrUtil.isBlank(shopJson)){
//            // 不存在则返回
//            return null;
//        }
//        //存在，判断是否过期
//        //json反序列化
//        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
//        Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(), Shop.class);
//        LocalDateTime expireTime = redisData.getExpireTime();
//        //判断是否过期
//        if (expireTime.isAfter(LocalDateTime.now())){
//            //未过期，直接返回店铺信息
//            return shop;
//        }
//        //已过期，缓存重建
//        //获取互斥锁
//        String lockKey = LOCK_SHOP_KEY + id;
//        boolean isLock = tryLock(lockKey);
//        //判断是否获取成功
//        if (isLock){
//            //成功，创建新的线程，独立执行缓存重建
//            CACHE_THREAD_POOL.submit(() -> {
//                try {
//                    //重建缓存
//                    this.saveShop2Redis(id, 20L);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }finally {
//                    //释放锁
//                    unLock(lockKey);
//                }
//            });
//        }
//        //失败，返回商铺信息（过期
//
//        //返回商铺信息
//        return shop;
//    }
//
//    //缓存空值到redis 缓存穿透
//    public Shop queryWithPassThrough(Long id) {
//        String key = CACHE_SHOP_KEY + id;
//        // 从redis查询商铺缓存
//        String shopJson = stringRedisTemplate.opsForValue().get(key);
//        //判断商户是否存在
//        if (StrUtil.isNotBlank(shopJson)){
//            // 存在则返回
//            return JSONUtil.toBean(shopJson, Shop.class);
//        }
//        //命中的是否是空值
//        if (shopJson != null){
//            return null;
//        }
//        // 不存在则查询数据库
//        Shop shop = getById( id);
//        // 不存在返回错误
//        if (shop == null){
//            //将空值写入redis
//            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
//            //返回错误信息
//            return null;
//        }
//        //存在，写入redis
//        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL, TimeUnit.MINUTES);
//        //返回商铺信息
//        return shop;
//    }

//    //尝试获取锁
//    private boolean tryLock(String key){
//        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
//        return BooleanUtil.isTrue(flag);
//    }
//    //释放锁
//    private void unLock(String key){
//        stringRedisTemplate.delete(key);
//    }

//    public void saveShop2Redis(Long id, Long expireSeconds){
//        //查询店铺数据
//        Shop shop = getById(id);
//        //封装逻辑过期时间
//        RedisData redisData = new RedisData();
//        redisData.setData(shop);
//        redisData.setExpireTime(LocalDateTime.now().plusSeconds(CACHE_SHOP_TTL));
//        //写入redis
//        stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY + id, JSONUtil.toJsonStr(redisData));
//    }

    @Override
    @Transactional
    public Result updated(Shop shop) {
        Long id = shop.getId();
        if (id == null){
            return Result.fail("店铺id不能为空");
        }
        //更新数据库
        updateById(shop);
        //删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + shop.getId());
        return Result.ok();
    }
}
