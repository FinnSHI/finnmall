package com.finn.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finn.gulimall.product.entity.SkuImagesEntity;
import com.finn.gulimall.product.service.AttrGroupService;
import com.finn.gulimall.product.service.SkuImagesService;
import com.finn.gulimall.product.service.SkuSaleAttrValueService;
import com.finn.gulimall.product.service.SpuInfoDescService;
import com.finn.gulimall.product.vo.SkuItemVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finn.common.utils.PageUtils;
import com.finn.common.utils.Query;

import com.finn.gulimall.product.dao.SkuInfoDao;
import com.finn.gulimall.product.entity.SkuInfoEntity;
import com.finn.gulimall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Resource
    private SkuImagesService skuImagesService;

    @Resource
    private SpuInfoDescService spuInfoDescService;

    @Resource
    private AttrGroupService attrGroupService;

    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    private ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        /**
         * key:
         * catelogId: 0
         * brandId: 0
         * min: 0
         * max: 0
         */
        String key = (String) params.get("key");
        if(!org.springframework.util.StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("sku_id",key).or().like("sku_name",key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if(!org.springframework.util.StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){

            queryWrapper.eq("catalog_id",catelogId);
        }

        String brandId = (String) params.get("brandId");
        if(!org.springframework.util.StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(catelogId)){
            queryWrapper.eq("brand_id",brandId);
        }

        String min = (String) params.get("min");
        if(!org.springframework.util.StringUtils.isEmpty(min)){
            queryWrapper.ge("price",min);
        }

        String max = (String) params.get("max");

        if(!StringUtils.isEmpty(max)  ){
            try{
                BigDecimal bigDecimal = new BigDecimal(max);

                if(bigDecimal.compareTo(new BigDecimal("0"))==1){
                    queryWrapper.le("price",max);
                }
            }catch (Exception e){

            }

        }


        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {

        return this.list(new LambdaQueryWrapper<SkuInfoEntity>().eq(SkuInfoEntity::getSpuId, spuId));
    }

    /*
    * @Description: 异步编排优化
    * @Param: [skuId]
    * @return: com.finn.gulimall.product.vo.SkuItemVO
    * @Author: Finn
    * @Date: 2022/07/06 21:17
    */
    @Override
    public SkuItemVO item(Long skuId) throws ExecutionException, InterruptedException {

        SkuItemVO skuItemVO = new SkuItemVO();

        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            // 1. sku基本信息的获取 pms_sku_info
            SkuInfoEntity info = this.getById(skuId);
            skuItemVO.setInfo(info);
            return info;
        }, executor);

        // infoFuture完成后就开始
        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            // 3. spu销售属性组合
            skuItemVO.setSaleAttr(skuSaleAttrValueService.getSaleAttrBySpuId(res.getSpuId()));
        }, executor);

        // infoFuture完成后就开始
        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            // 4. spu的介绍
            skuItemVO.setDesc(spuInfoDescService.getById(res.getSpuId()));
        }, executor);

        // infoFuture完成后就开始
        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            // 5. spu的规格参数信息
            skuItemVO.setGroupAttrs(attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(),
                    res.getCatalogId()));
        }, executor);

        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            // 2. sku的图片信息 pms_sku_imgs
            List<SkuImagesEntity> images = skuImagesService.getImageBySkuId(skuId);
            skuItemVO.setImages(images);
        }, executor);

        // 等待所有任务都完成
        CompletableFuture.allOf(saleAttrFuture, descFuture, baseAttrFuture, imageFuture).get();

        return skuItemVO;
    }
}