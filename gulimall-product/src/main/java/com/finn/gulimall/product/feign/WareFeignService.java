package com.finn.gulimall.product.feign;

import com.finn.common.to.SkuHasStockVO;
import com.finn.common.utils.CommonResult;
import com.finn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/*
 * @description: 远程调用库存接口
 * @author: Finn
 * @create: 2022/06/20 21:08
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {

    @PostMapping("/ware/waresku/hasstock")
    CommonResult<List<SkuHasStockVO>> getSkuHasStock(@RequestBody List<Long> skuIds);
}
