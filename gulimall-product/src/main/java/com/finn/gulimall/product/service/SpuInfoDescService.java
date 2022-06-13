package com.finn.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finn.common.utils.PageUtils;
import com.finn.gulimall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-24 14:04:02
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity);
}

