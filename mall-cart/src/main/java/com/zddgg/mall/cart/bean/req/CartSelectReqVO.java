package com.zddgg.mall.cart.bean.req;

import lombok.Data;

import java.util.List;

@Data
public class CartSelectReqVO {

    // 0-取消单个，1-选中单个，2-取消所有，3-取消所有
    private String actionType;

    private List<String> cartIds;
}
