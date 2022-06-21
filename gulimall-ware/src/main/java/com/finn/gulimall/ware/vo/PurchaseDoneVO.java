package com.finn.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PurchaseDoneVO {

    @NotNull
    private Long id;//采购单id

    private List<PurchaseItemDoneVO> items;
}
