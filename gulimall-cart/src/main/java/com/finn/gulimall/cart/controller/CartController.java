package com.finn.gulimall.cart.controller;

import com.finn.gulimall.cart.service.CartService;
import com.finn.gulimall.cart.vo.CartItemVO;
import com.finn.gulimall.cart.vo.CartVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/*
 * @description:
 * @author: Finn
 * @create: 2022/07/10 13:58
 */
@Controller
public class CartController {

    @Resource
    private CartService cartService;

    /* 
    * 1. 浏览器有个cookie：user-key：标识用户身份，该cookie一个月后过期
    *    如果第一次使用购物车，会给一个临时身份，浏览器保存该cookie，每次访问都会带上
    *
    * 使用拦截器完成功能：
    *   登录：session
    *   未登录：使用cookie的user-key
    *   第一次登录，创建临时用户
    */
    @GetMapping(value = "/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        // 快速得到用户信息：id,user-key
        // UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();

        CartVO cartVO = cartService.getCart();
        model.addAttribute("cart", cartVO);
        return "cartList";
    }

    /**
     * 添加商品到购物车
     * attributes.addFlashAttribute():将数据放在session中，可以在页面中取出，但是只能取一次
     * attributes.addAttribute():将数据放在url后面
     * @return
     */
    @GetMapping(value = "/addCartItem")
    public String addCartItem(@RequestParam("skuId") Long skuId,
                              @RequestParam("num") Integer num,
                              RedirectAttributes attributes) throws ExecutionException, InterruptedException {

        cartService.addToCart(skuId, num);

        attributes.addAttribute("skuId", skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccessPage.html";
    }

    /**
     * 跳转到添加购物车成功页面
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping(value = "/addToCartSuccessPage.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,
                                       Model model) {
        //重定向到成功页面。再次查询购物车数据即可
        CartItemVO cartItemVo = cartService.getCartItem(skuId);
        model.addAttribute("cartItem", cartItemVo);

        return "success";
    }

    /**
     * 商品是否选中
     * @param skuId
     * @param checked
     * @return
     */
    @GetMapping(value = "/checkItem")
    public String checkItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "checked") Integer checked) {

        cartService.checkItem(skuId,checked);

        return "redirect:http://cart.gulimall.com/cart.html";

    }


    /**
     * 改变商品数量
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping(value = "/countItem")
    public String countItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "num") Integer num) {

        cartService.changeItemCount(skuId,num);

        return "redirect:http://cart.gulimall.com/cart.html";
    }


    /**
     * 删除商品信息
     * @param skuId
     * @return
     */
    @GetMapping(value = "/deleteItem")
    public String deleteItem(@RequestParam("skuId") Integer skuId) {

        cartService.deleteIdCartInfo(skuId);

        return "redirect:http://cart.gulimall.com/cart.html";

    }
}