package com.finn.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finn.common.utils.PageUtils;
import com.finn.gulimall.product.entity.AttrGroupEntity;
import com.finn.gulimall.product.vo.AttrGroupWithAttrsVO;
import com.finn.gulimall.product.vo.SpuItemAttrGroupVO;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-24 14:04:03
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrGroupWithAttrsVO> getAttrGroupWithAttrsByCatelogId(Long catelogId);
}

