package com.zddgg.mall.cart.common;

import lombok.Getter;

@Getter
public enum CartSelectedFlag {

    NOT_SELECTED("0", "未选中"),

    SELECTED("1", "未选中");

    private final String code;

    private final String msg;

    CartSelectedFlag(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
