package com.finn.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finn.gulimall.product.entity.CategoryEntity;
import com.finn.gulimall.product.service.CategoryService;
import com.finn.common.utils.R;



/**
 * 商品三级分类
 *
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-24 15:49:11
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /* 
    * @Description: 查出所有分类，并组装成三级树形结构 
    * @Param: [] 
    * @return: com.finn.common.utils.R 
    * @Author: Finn
    * @Date: 2022/04/27 10:12
    */
    @RequestMapping("/list/tree")
    public R listTree() {
        List<CategoryEntity> entityList =  categoryService.listWithTree();
        return R.ok().put("data", entityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category){

        categoryService.updateCascade(category);

        return R.ok();
    }

    /*
    * @Description: 拖拽菜单，修改排序
    * @Param: [category]
    * @return: com.finn.common.utils.R
    * @Author: Finn
    * @Date: 2022/04/28 16:38
    */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] categories){
        categoryService.updateBatchById(Arrays.asList(categories));
        return R.ok();
    }

    /**
     * 删除
     * @RequestBody: post
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){
//		categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenuByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
