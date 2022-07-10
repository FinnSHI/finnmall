package com.finn.gulimall.product.service.impl;

import com.finn.gulimall.product.vo.SkuItemSaleAttrVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finn.common.utils.PageUtils;
import com.finn.common.utils.Query;

import com.finn.gulimall.product.dao.SkuSaleAttrValueDao;
import com.finn.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.finn.gulimall.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVO> getSaleAttrBySpuId(Long spuId) {

        SkuSaleAttrValueDao baseMapper = this.getBaseMapper();

        return baseMapper.getSaleAttrBySpuId(spuId);
    }

    @Override
    public List<String> getSkuSaleAttrValuesAsStringList(Long skuId) {

        List<String> stringList = this.baseMapper.getSkuSaleAttrValuesAsStringList(skuId);

        return stringList;
    }
}