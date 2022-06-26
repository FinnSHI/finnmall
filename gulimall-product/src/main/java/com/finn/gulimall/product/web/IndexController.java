package com.finn.gulimall.product.web;

import com.finn.gulimall.product.entity.CategoryEntity;
import com.finn.gulimall.product.service.CategoryService;
import com.finn.gulimall.product.vo.CatelogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/*
 * @description: 首页跳转
 * @author: Finn
 * @create: 2022/06/25 13:12
 */
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = {"/","/index.html"})
    public String indexPage(Model model) {

        List<CategoryEntity> categories = categoryService.getLevel1Categories();

        model.addAttribute("categories", categories);
        // 视图解析器进行拼串
        // /templates/index.html
        return "index";
    }

    @GetMapping(value = "/index/catalog.json")
    public Object getCatalogJson() {
        Map<String, List<CatelogVO>> catalogJson = categoryService.getCatalogJson();

        return catalogJson;
    }
}
