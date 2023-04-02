package com.zddgg.mall.oms.constant;

public enum StateFlagEnum {
    DELETED("-1", "已删除"),

    DISABLED("0", "已停用"),

    ENABLED("1", "已启用"),

    ;

    public final String code;

    public final String msg;

    StateFlagEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
