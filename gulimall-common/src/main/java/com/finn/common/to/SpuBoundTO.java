package com.finn.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-01 08:45
 **/

@Data
public class SpuBoundTO {

    private Long spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;

}
