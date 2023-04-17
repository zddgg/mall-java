package com.zddgg.mall.cart.bean.req;

import lombok.Data;

@Data
public class SkuNumUpdateReqVO {

    // 0-减1， 1-加1
    private String actionType;

    private String cartId;
}
