package com.finn.gulimall.seckill.feign;

import com.finn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * @description: 远程调用 gulimall-coupon
 * @author: Finn
 * @create: 2022/08/08 11:23
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     * 查询最近三天需要参加秒杀商品的信息
     * @return
     */
    @GetMapping(value = "/coupon/seckillsession/Latest3DaySession")
    R getLatest3DaySession();
}
