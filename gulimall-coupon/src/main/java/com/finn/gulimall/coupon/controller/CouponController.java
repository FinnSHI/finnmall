package com.finn.gulimall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finn.gulimall.coupon.entity.CouponEntity;
import com.finn.gulimall.coupon.service.CouponService;
import com.finn.common.utils.PageUtils;
import com.finn.common.utils.R;



/**
 * 优惠券信息
 *
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-25 22:59:21
 */
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    /*
    * @Description: 返回会员的所有优惠券
    * @Param: []
    * @return: com.finn.common.utils.R
    * @Author: Finn
    * @Date: 2022/04/26 21:29
    */
    @RequestMapping("/member/list")
    public R memberCoupons() {
        return R.ok().put("coupons", Arrays.asList(CouponEntity.builder().couponName("满100减10").build()));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CouponEntity coupon){
		couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CouponEntity coupon){
		couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
