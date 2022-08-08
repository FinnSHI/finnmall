package com.finn.gulimall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.finn.common.utils.R;
import com.finn.gulimall.seckill.entity.SeckillSkuRedisEntity;
import com.finn.gulimall.seckill.feign.CouponFeignService;
import com.finn.gulimall.seckill.feign.ProductFeignService;
import com.finn.gulimall.seckill.service.SeckillService;
import com.finn.gulimall.seckill.vo.SeckillSessionWithSkusVO;
import com.finn.gulimall.seckill.vo.SkuInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * @description:
 * @author: Finn
 * @create: 2022/08/08 11:18
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private ProductFeignService productFeignService;

    // 商品活动前缀
    private final String SESSION_CACHE_PREFIX = "seckill:sessions:";

    // 秒杀商品前缀
    private final String SECKILL_CHARE_PREFIX = "seckill:skus";

    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";    //+商品随机码

    @Override
    public void uploadSeckillSkuLatest3Days() {
        // 1.扫描最近三天要参加秒杀的活动
        R latest3DaySession = couponFeignService.getLatest3DaySession();
        if (latest3DaySession.getCode() == 0) {
            // 2.上架商品
            // 2.1 得到数据
            List<SeckillSessionWithSkusVO> sessionData = latest3DaySession.getData("data", new TypeReference<List<SeckillSessionWithSkusVO>>() {
            });
            // 2.2 缓存到Redis
            // 2.2.1 缓存秒杀活动信息
            saveSessionInfos(sessionData);

            // 2.2.2 缓存活动的关联的商品信息
            saveSessionSkuInfo(sessionData);
        }
    }

    /*
    * 保存活动信息
    * key: ”开始时间-结束时间“
    * value: ”List<商品id>“
    * */
    private void saveSessionInfos(List<SeckillSessionWithSkusVO> sessionData) {
        sessionData.stream().forEach(session -> {

            // 获取当前活动的开始和结束时间的时间戳
            long startTime = session.getStartTime().getTime();
            long endTime = session.getEndTime().getTime();

            // 存入到 Redis 中的 key
            String key = SESSION_CACHE_PREFIX + startTime + "_" + endTime;
            //判断Redis中是否有该信息，如果没有才进行添加
            Boolean hasKey = redisTemplate.hasKey(key);
            //缓存活动信息
            if (!hasKey) {
                //获取到活动中所有商品的skuId
                List<String> skuIds = session.getRelationSkus().stream()
                        .map(item -> item.getPromotionSessionId() + "-" + item.getSkuId().toString()).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key,skuIds);
            }
        });
    }

    /*
    * 保存商品信息
    * key: seckill_sku表的 id
    * value: SeckillSkuRedisEntity的JSON格式
    * */
    private void saveSessionSkuInfo(List<SeckillSessionWithSkusVO> sessionData) {
        sessionData.stream().forEach(session -> {
            //准备hash操作，绑定hash
            BoundHashOperations<String, Object, Object> opts = redisTemplate.boundHashOps(SECKILL_CHARE_PREFIX);
            session.getRelationSkus().stream().forEach(seckillSkuVO -> {
                // 1.生成随机码
                String token = UUID.randomUUID().toString().replace("-", "");

                // 2.缓存数据
                SeckillSkuRedisEntity redisEntity = new SeckillSkuRedisEntity();
                Long skuId = seckillSkuVO.getSkuId();

                // 2.1 sku基本数据
                // 2.1.1 远程调用gulimall-product, 并获取sku信息
                R info = productFeignService.getSkuInfo(skuId);
                if (info.getCode() == 0) {
                    SkuInfoVO skuInfo = info.getData("skuInfo",new TypeReference<SkuInfoVO>(){});
                    redisEntity.setSkuInfo(skuInfo);
                }

                // 2.2 sku秒杀信息
                BeanUtils.copyProperties(seckillSkuVO, redisEntity);

                // 2.3 设置上商品的秒杀时间信息
                redisEntity.setStartTime(session.getStartTime().getTime());
                redisEntity.setEndTime(session.getEndTime().getTime());

                // 2.4 随机码：为每一个商品设置随机码，可以防止脚本抢货
                redisEntity.setRandomCode(token);

                String redisEntityJson = JSON.toJSONString(redisEntity);
                opts.put(seckillSkuVO.getPromotionSessionId().toString() + "-" +seckillSkuVO.getId().toString(), redisEntityJson);
            });
        });
    }
}
