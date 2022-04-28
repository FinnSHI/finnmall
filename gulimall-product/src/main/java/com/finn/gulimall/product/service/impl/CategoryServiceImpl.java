package com.finn.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finn.common.utils.PageUtils;
import com.finn.common.utils.Query;

import com.finn.gulimall.product.dao.CategoryDao;
import com.finn.gulimall.product.entity.CategoryEntity;
import com.finn.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /*
    * @Description: 查出所有分类，并组装成三级树形结构
     * @Param: []
    * @return: java.util.List<com.finn.gulimall.product.entity.CategoryEntity>
    * @Author: Finn
    * @Date: 2022/04/27 10:14
    */
    @Override
    public List<CategoryEntity> listWithTree() {
        // 所有分类
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(null);
        // 一级分类
        List<CategoryEntity> levelFirstMenuList = categoryEntities.stream()
                .filter((categoryEntity) -> categoryEntity.getParentCid() == 0)
                .map((menu) -> {
                    menu.setChildren(this.listChildren(menu, categoryEntities));
                    return menu;
                })
                .sorted((menu1, menu2) -> (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort()))
                .collect(Collectors.toList())
                ;
        //

        return levelFirstMenuList;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO 检查当前删除的菜单，是否被其他地方引用
        this.baseMapper.deleteBatchIds(asList);
    }

    /*
    * @Description: *递归* 查找子菜单
    * @Param: [root, categoryEntities]
    * @return: java.util.List<com.finn.gulimall.product.entity.CategoryEntity>
    * @Author: Finn
    * @Date: 2022/04/27 11:08
    */
    private List<CategoryEntity> listChildren(CategoryEntity root, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> childrenList = categoryEntities.stream()
                .filter((categoryEntity) -> categoryEntity.getParentCid() == root.getCatId())
                .map((menu) -> {
                    menu.setChildren(listChildren(menu, categoryEntities));
                    return menu;
                })
                .sorted((menu1, menu2) -> (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort()))
                .collect(Collectors.toList());
        return childrenList;
    }

}