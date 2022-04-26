package com.finn.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.finn.gulimall.product.entity.BrandEntity;
import com.finn.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Test
    void contextLoads() {
//        BrandEntity brand = BrandEntity.builder().brandId(13L).descript("vivo").name("vivo").build();
//        brandService.updateById(brand);
        System.out.println("success");
        List<BrandEntity> brand_id = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 9L));
        brand_id.forEach(System.out::println);
    }

}
