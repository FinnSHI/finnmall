package com.finn.gulimall.order.dao;

import com.finn.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-25 23:12:40
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
