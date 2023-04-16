package com.zddgg.mall.cart.exception;

import com.zddgg.mall.common.enums.SystemEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BizException extends RuntimeException {

    private String code;

    private String msg;

    private Object data;

    public BizException(String msg) {
        super(msg);
        this.code = SystemEnum.SYSTEM_EX.code;
        this.msg = msg;
    }

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BizException(String code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
