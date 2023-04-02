package com.zddgg.mall.product.advice;

import com.zddgg.mall.common.enums.SystemEnum;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.exception.BizException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public Result<Object> handlerBusinessException(DataIntegrityViolationException ex) {
        log.error(" DataIntegrityViolationException ", ex);
        return Result.fail(SystemEnum.DB_EX);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result<Object> handlerBusinessException(BindException ex) {
        log.error(" BindException ", ex);
        return Result.fail(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());
    }

    /**
     * 参数异常.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<Object> handlerBusinessException(MethodArgumentNotValidException ex) {
        log.error(" MethodArgumentNotValidException ", ex);
        return Result.fail(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());
    }

    /**
     * 参数异常.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result<Object> handlerBusinessException(ConstraintViolationException ex) {
        log.error(" ConstraintViolationException ", ex);
        return Result.fail(ex.getConstraintViolations().iterator().next().getMessage());
    }

    /**
     * 业务异常.
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public Result<Object> handlerBusinessException(BizException ex) {
        log.error(" BizException ", ex);
        return Result.custom(ex.getCode(), ex.getMsg(), ex.getData());
    }

    /**
     * 其它异常.
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Result<Object> handlerThrowable(Throwable th) {
        log.error("error", th);
        return Result.fail();
    }
}
