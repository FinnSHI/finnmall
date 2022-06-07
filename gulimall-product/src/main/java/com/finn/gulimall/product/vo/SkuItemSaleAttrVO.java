package com.finn.gulimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-19 18:17
 **/

@Data
@ToString
public class SkuItemSaleAttrVO {

    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVO> attrValues;

}
