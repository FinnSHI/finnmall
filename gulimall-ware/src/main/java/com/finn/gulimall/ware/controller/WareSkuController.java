package com.finn.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.finn.common.to.SkuHasStockVO;
import com.finn.common.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finn.gulimall.ware.entity.WareSkuEntity;
import com.finn.gulimall.ware.service.WareSkuService;
import com.finn.common.utils.PageUtils;
import com.finn.common.utils.R;



/**
 * 商品库存
 *
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-25 23:16:18
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {

    @Autowired
    private WareSkuService wareSkuService;

    /*
    * @Description: 查询sku是否有库存
    * @Param:
    * @return:
    * @Author: Finn
    * @Date: 2022/06/20 20:39
    */
    @PostMapping("/hasstock")
    public CommonResult<List<SkuHasStockVO>> getSkuHasStock(@RequestBody List<Long> skuIds){

        List<SkuHasStockVO> vos = wareSkuService.getSkusHasStock(skuIds);

        return CommonResult.success(vos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
