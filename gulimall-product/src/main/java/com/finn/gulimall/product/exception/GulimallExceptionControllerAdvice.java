package com.finn.gulimall.product.exception;

import com.finn.common.enums.BizCodeEnum;
import com.finn.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/*
 * @description: 集中处理Controller异常
 * @author: Finn
 * @create: 2022/05/03 14:20
 */
@Slf4j
//@ResponseBody
//@ControllerAdvice(basePackages = "com.finn.gulimall.product.controller")
@RestControllerAdvice(basePackages = "com.finn.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据效验出现问题{},异常类型{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> retMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError) -> {
            retMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(), "数据校验失败").put("data", retMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {

        log.error("错误异常{}", throwable);

        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(), BizCodeEnum.UNKNOW_EXCEPTION.getMessage());
    }
}
