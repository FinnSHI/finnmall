package com.finn.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finn.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

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
    * @Description: 找到cateLogId的完整路径
    * @Param: [catelogId]
    * @return: java.lang.Long[]
    * @Author: Finn
    * @Date: 2022/05/23 21:26
    */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return (Long[])parentPath.toArray();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCascade(CategoryEntity category) {
        try {
            this.baseMapper.updateById(category);
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //同时修改缓存中的数据
        //删除缓存,等待下一次主动查询进行更新
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        // 1.收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid() != 0) {
            findParentPath(byId.getCatId(), paths);
        }
        return paths;
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