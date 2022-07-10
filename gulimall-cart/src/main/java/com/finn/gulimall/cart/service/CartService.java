package com.finn.gulimall.cart.service;

import com.finn.gulimall.cart.vo.CartItemVO;
import com.finn.gulimall.cart.vo.CartVO;

import java.util.List;
import java.util.concurrent.ExecutionException;

/*
 * @description:
 * @author: Finn
 * @create: 2022/07/07 21:13
 */
public interface CartService {

    CartItemVO addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItemVO getCartItem(Long skuId);

    /**
     * 获取购物车里面的信息
     * @return
     */
    CartVO getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车的数据
     * @param cartKey
     */
    public void clearCartInfo(String cartKey);

    /**
     * 勾选购物项
     * @param skuId
     * @param check
     */
    void checkItem(Long skuId, Integer check);

    /**
     * 改变商品数量
     * @param skuId
     * @param num
     */
    void changeItemCount(Long skuId, Integer num);


    /**
     * 删除购物项
     * @param skuId
     */
    void deleteIdCartInfo(Integer skuId);

    List<CartItemVO> getUserCartItems();
}
