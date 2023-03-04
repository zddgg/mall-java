package com.zddgg.mall.product.constant.brand;

public enum BrandLevel {

    NONE("0", "未定级"),
    B("1", "B级"),
    A("2", "A级"),
    S("3", "S级"),
    ;

    public final String code;

    public final String msg;

    BrandLevel(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
