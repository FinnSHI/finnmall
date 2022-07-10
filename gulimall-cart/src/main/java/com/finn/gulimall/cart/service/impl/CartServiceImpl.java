package com.finn.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.finn.common.utils.R;
import com.finn.gulimall.cart.exception.CartExceptionHandler;
import com.finn.gulimall.cart.feign.ProductFeignService;
import com.finn.gulimall.cart.interceptor.CartInterceptor;
import com.finn.gulimall.cart.service.CartService;
import com.finn.gulimall.cart.to.UserInfoTO;
import com.finn.gulimall.cart.vo.CartItemVO;
import com.finn.gulimall.cart.vo.CartVO;
import com.finn.gulimall.cart.vo.SkuInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static com.finn.common.constant.CartConstant.CART_PREFIX;

/*
 * @description:
 * @author: Finn
 * @create: 2022/07/07 21:14
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private ThreadPoolExecutor executor;

    /*
    * @Description: 添加到购物车
    * @Param: [skuId, num]
    * @return: void
    * @Author: Finn
    * @Date: 2022/07/10 16:11
    */
    @Override
    public CartItemVO addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        //拿到要操作的购物车信息
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        //判断Redis是否有该商品的信息
        String productRedisValue = (String) cartOps.get(skuId.toString());
        //如果没有就添加数据
        if (StringUtils.isEmpty(productRedisValue)) {

            //2、添加新的商品到购物车(redis)
            CartItemVO cartItemVO = new CartItemVO();
            //开启第一个异步任务
            CompletableFuture<Void> getSkuInfoFuture = CompletableFuture.runAsync(() -> {
                //1、远程查询当前要添加商品的信息
                R productSkuInfo = productFeignService.getInfo(skuId);
                SkuInfoVO skuInfo = productSkuInfo.getData("skuInfo", new TypeReference<SkuInfoVO>() {});
                //数据赋值操作
                cartItemVO.setSkuId(skuInfo.getSkuId());
                cartItemVO.setTitle(skuInfo.getSkuTitle());
                cartItemVO.setImage(skuInfo.getSkuDefaultImg());
                cartItemVO.setPrice(skuInfo.getPrice());
                cartItemVO.setCount(num);
            }, executor);

            //开启第二个异步任务
            CompletableFuture<Void> getSkuAttrValuesFuture = CompletableFuture.runAsync(() -> {
                //2、远程查询skuAttrValues组合信息
                List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
                cartItemVO.setSkuAttrValues(skuSaleAttrValues);
            }, executor);

            //等待所有的异步任务全部完成
            CompletableFuture.allOf(getSkuInfoFuture, getSkuAttrValuesFuture).get();

            String cartItemJson = JSON.toJSONString(cartItemVO);
            // 放入redis
            cartOps.put(skuId.toString(), cartItemJson);

            return cartItemVO;
        } else {
            //购物车有此商品，修改数量即可
            CartItemVO cartItemVO = JSON.parseObject(productRedisValue, CartItemVO.class);
            cartItemVO.setCount(cartItemVO.getCount() + num);
            //修改redis的数据
            String cartItemJson = JSON.toJSONString(cartItemVO);
            cartOps.put(skuId.toString(),cartItemJson);

            return cartItemVO;
        }
    }

    @Override
    public CartItemVO getCartItem(Long skuId) {
        //拿到要操作的购物车信息
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        String redisValue = (String) cartOps.get(skuId.toString());

        CartItemVO cartItemVo = JSON.parseObject(redisValue, CartItemVO.class);

        return cartItemVo;
    }

    /**
     * 获取到我们要操作的购物车
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        //先得到当前用户信息
        UserInfoTO user = CartInterceptor.threadLocal.get();

        String cartKey = "";
        if (user.getUserId() != null) {
            // gulimall:cart:1
            cartKey = CART_PREFIX + user.getUserId();
        } else {
            cartKey = CART_PREFIX + user.getUserKey();
        }

        //绑定指定的key操作Redis
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);

        return operations;
    }

    /**
     * 获取用户登录或者未登录购物车里所有的数据
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public CartVO getCart() throws ExecutionException, InterruptedException {

        CartVO cartVo = new CartVO();
        UserInfoTO userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() != null) {
            //1、已经登录
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            // 临时购物车的键
            String temptCartKey = CART_PREFIX + userInfoTo.getUserKey();

            // 2、如果临时购物车的数据还未进行合并
            List<CartItemVO> tempCartItems = getCartItems(temptCartKey);
            if (tempCartItems != null) {
                //临时购物车有数据需要进行合并操作
                for (CartItemVO item : tempCartItems) {
                    addToCart(item.getSkuId(),item.getCount());
                }
                //清除临时购物车的数据
                clearCartInfo(temptCartKey);
            }

            //3、获取登录后的购物车数据【包含合并过来的临时购物车的数据和登录后购物车的数据】
            List<CartItemVO> cartItems = getCartItems(cartKey);
            cartVo.setItems(cartItems);

        } else {
            //没登录
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车里面的所有购物项
            List<CartItemVO> cartItems = getCartItems(cartKey);
            cartVo.setItems(cartItems);
        }

        return cartVo;
    }

    /**
     * 获取购物车里面的数据
     * @param cartKey
     * @return
     */
    private List<CartItemVO> getCartItems(String cartKey) {

        //获取购物车里面的所有商品
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        List<Object> values = operations.values();
        if (values != null && values.size() > 0) {
            List<CartItemVO> cartItemVoStream = values.stream().map((obj) -> {
                String str = (String) obj;
                CartItemVO cartItem = JSON.parseObject(str, CartItemVO.class);
                return cartItem;
            }).collect(Collectors.toList());
            return cartItemVoStream;
        }
        return null;

    }


    @Override
    public void clearCartInfo(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {

        //查询购物车里面的商品
        CartItemVO cartItem = getCartItem(skuId);
        //修改商品状态
        cartItem.setCheck(check == 1?true:false);

        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItem);

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(),redisValue);

    }

    /**
     * 修改购物项数量
     * @param skuId
     * @param num
     */
    @Override
    public void changeItemCount(Long skuId, Integer num) {

        //查询购物车里面的商品
        CartItemVO cartItem = getCartItem(skuId);
        cartItem.setCount(num);

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),redisValue);
    }


    /**
     * 删除购物项
     * @param skuId
     */
    @Override
    public void deleteIdCartInfo(Integer skuId) {

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    @Override
    public List<CartItemVO> getUserCartItems() {

        List<CartItemVO> cartItemVoList = new ArrayList<>();
        //获取当前用户登录的信息
        UserInfoTO userInfoTo = CartInterceptor.threadLocal.get();
        //如果用户未登录直接返回null
        if (userInfoTo.getUserId() == null) {
            return null;
        } else {
            //获取购物车项
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            //获取所有的
            List<CartItemVO> cartItems = getCartItems(cartKey);
            if (cartItems == null) {
                throw new CartExceptionHandler();
            }
            //筛选出选中的
            cartItemVoList = cartItems.stream()
                    .filter(items -> items.getCheck())
                    .map(item -> {
                        //更新为最新的价格（查询数据库）
                        BigDecimal price = productFeignService.getPrice(item.getSkuId());
                        item.setPrice(price);
                        return item;
                    })
                    .collect(Collectors.toList());
        }

        return cartItemVoList;
    }
}
