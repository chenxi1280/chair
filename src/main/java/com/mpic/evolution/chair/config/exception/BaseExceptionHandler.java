package com.mpic.evolution.chair.config.exception;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author by cxd
 * @Classname BaseExceptionHandler
 * @Description TODO
 * @Date 2020/12/16 15:36
 */
//@ControllerAdvice
public class BaseExceptionHandler {
    // 捕获参数异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    ResponseDTO  baseExceptionException(Exception e) {
        return ResponseDTO.fail(e.getMessage(),"后台错误",550,550);
    }

}
