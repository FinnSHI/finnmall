package com.finn.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finn.common.utils.PageUtils;
import com.finn.gulimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-24 14:04:03
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

