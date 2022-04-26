package com.finn.gulimall.member.feign;

import com.finn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * @description: 远程调用Coupon接口
 * @author: Finn
 * @create: 2022/04/26 21:37
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupons();

}
