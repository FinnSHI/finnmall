package com.finn.gulimall.product.vo;

import lombok.Data;

/*
 * @description:
 * @author: Finn
 * @create: 2022/05/31 20:26
 */
@Data
public class AttrRespVO extends AttrVO {

    /**
     *
     */
    private String catelogName;

    private String groupName;

    private Long[] catelogPath;

}
