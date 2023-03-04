package com.zddgg.mall.product.exception;

import com.voidtime.mall.common.enums.SystemEnum;
import com.voidtime.mall.product.constant.RespEnum;
import lombok.Data;

@Data
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

    public BizException(RespEnum respEnum) {
        super(respEnum.msg);
        this.code = respEnum.code;
        this.msg = respEnum.msg;
        this.data = null;
    }

    public BizException(RespEnum respEnum, Object data) {
        super(respEnum.msg);
        this.code = respEnum.code;
        this.msg = respEnum.msg;
        this.data = data;
    }
}
