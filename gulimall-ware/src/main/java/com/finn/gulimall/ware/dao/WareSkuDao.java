package com.finn.gulimall.ware.dao;

import com.finn.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-25 23:16:18
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
