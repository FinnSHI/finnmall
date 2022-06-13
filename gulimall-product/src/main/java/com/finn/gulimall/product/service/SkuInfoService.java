package com.finn.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finn.common.utils.PageUtils;
import com.finn.gulimall.product.entity.SkuInfoEntity;
import com.finn.gulimall.product.vo.SkuItemVO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-24 14:04:02
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

