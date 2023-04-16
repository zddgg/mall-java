package com.zddgg.mall.cart.common;

public enum StateFlag {
    DELETED("-1", "已删除"),

    DISABLED("0", "已停用"),

    ENABLED("1", "已启用"),

    ;

    public final String code;

    public final String msg;

    StateFlag(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
