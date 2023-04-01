package com.zddgg.mall.common.response;

import com.zddgg.mall.common.enums.SystemEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class Result<T> {

    private String code;

    private String msg;

    private T data;

    private Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return custom(SystemEnum.SUCCESS.code, SystemEnum.SUCCESS.msg, null);
    }

    public static <T> Result<T> success(String msg) {
        return custom(SystemEnum.SUCCESS.code, msg, null);
    }

    public static <T> Result<T> success(T data) {
        return custom(SystemEnum.SUCCESS.code, SystemEnum.SUCCESS.msg, data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return custom(SystemEnum.SUCCESS.code, msg, data);
    }

    public static <T> Result<T> fail() {
        return custom(SystemEnum.SYSTEM_EX.code, SystemEnum.SYSTEM_EX.msg, null);
    }

    public static <T> Result<T> fail(String msg) {
        return custom(SystemEnum.SYSTEM_EX.code, msg, null);
    }

    public static <T> Result<T> fail(String code, String msg) {
        return custom(code, msg, null);
    }

    public static <T> Result<T> fail(SystemEnum systemEnum) {
        return custom(systemEnum.code, systemEnum.msg, null);
    }

    public static <T> Result<T> custom(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }
}
