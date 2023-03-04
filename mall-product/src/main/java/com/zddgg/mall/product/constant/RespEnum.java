package com.zddgg.mall.product.constant;

public enum RespEnum {

    BRAND_EXIST("10000", "品牌已存在！");
    public final String code;

    public final String msg;

    RespEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
