package com.finn.gulimall.coupon.dao;

import com.finn.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-25 22:59:21
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
