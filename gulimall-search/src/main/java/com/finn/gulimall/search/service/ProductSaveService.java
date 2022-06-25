package com.finn.gulimall.search.service;

import com.finn.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/*
 * @description: 商品服务
 * @author: Finn
 * @create: 2022/06/21 19:32
 */
public interface ProductSaveService {

    Boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
