package com.zddgg.mall.product.constant;

public enum StatusEnum {
    DELETED("-1", "已删除"),

    DISABLED("0", "已停用"),

    ENABLED("1", "已启用"),

    ;

    public final String code;

    public final String msg;

    StatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
