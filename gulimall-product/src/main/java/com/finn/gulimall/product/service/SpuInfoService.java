package com.finn.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finn.common.utils.PageUtils;
import com.finn.gulimall.product.entity.SpuInfoEntity;
import com.finn.gulimall.product.vo.SpuSaveVO;

import java.util.Map;

/**
 * spu信息
 *
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-24 14:04:02
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVO vo);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

