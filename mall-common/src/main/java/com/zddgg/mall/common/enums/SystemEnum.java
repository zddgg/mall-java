package com.zddgg.mall.common.enums;

public enum SystemEnum {

    SUCCESS("0000", "操作成功"),

    PARAM_EX("0001", "参数异常"),

    DB_EX("0002", "数据库异常"),

    SYSTEM_EX("9999", "系统异常"),

    ;
    public final String code;

    public final String msg;

    SystemEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
