package com.finn.gulimall.product.web;

import com.finn.gulimall.product.service.SkuInfoService;
import com.finn.gulimall.product.vo.SkuItemVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/*
 * @description: 商品详情信息
 * @author: Finn
 * @create: 2022/07/05 21:39
 */
@Controller
public class ItemController {

    @Resource
    private SkuInfoService skuInfoService;

    /**
     * 展示当前sku的详情
     * @param skuId
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId,
                          Model model) throws ExecutionException, InterruptedException {

        System.out.println("准备查询" + skuId + "详情");

        SkuItemVO vos = skuInfoService.item(skuId);

        model.addAttribute("item", vos);

        return "item";
    }
}
