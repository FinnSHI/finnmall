package com.finn.gulimall.product.feign;

import com.finn.common.to.es.SkuEsModel;
import com.finn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/*
 * @description: ES
 * @author: Finn
 * @create: 2022/06/21 19:57
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")

    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
