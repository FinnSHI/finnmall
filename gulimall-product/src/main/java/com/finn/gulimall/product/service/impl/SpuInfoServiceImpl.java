package com.finn.gulimall.product.service.impl;

import com.finn.gulimall.product.feign.CouponFeignService;
import com.finn.gulimall.product.feign.SearchFeignService;
import com.finn.gulimall.product.feign.WareFeignService;
import com.finn.gulimall.product.service.AttrService;
import com.finn.gulimall.product.service.BrandService;
import com.finn.gulimall.product.service.CategoryService;
import com.finn.gulimall.product.service.ProductAttrValueService;
import com.finn.gulimall.product.service.SkuImagesService;
import com.finn.gulimall.product.service.SkuInfoService;
import com.finn.gulimall.product.service.SkuSaleAttrValueService;
import com.finn.gulimall.product.service.SpuImagesService;
import com.finn.gulimall.product.service.SpuInfoDescService;
import com.finn.gulimall.product.vo.SpuSaveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finn.common.utils.PageUtils;
import com.finn.common.utils.Query;

import com.finn.gulimall.product.dao.SpuInfoDao;
import com.finn.gulimall.product.entity.SpuInfoEntity;
import com.finn.gulimall.product.service.SpuInfoService;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void savesupInfo(SpuSaveVO vo) {

    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {

    }

    @Override
    public PageUtils queryPageByCondtion(Map<String, Object> params) {
        return null;
    }

    @Override
    public void up(Long spuId) {

    }

    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        return null;
    }

}