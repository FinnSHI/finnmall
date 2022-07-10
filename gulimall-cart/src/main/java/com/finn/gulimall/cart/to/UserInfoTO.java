package com.finn.gulimall.cart.to;

import lombok.Data;

/*
 * @description:
 * @author: Finn
 * @create: 2022/07/10 14:10
 */
@Data
public class UserInfoTO {

    private Long userId;

    private String userKey;

    /**
     * 是否临时用户
     */
    private Boolean tempUser = false;
}
