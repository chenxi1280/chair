package com.mpic.evolution.chair.config.exception;

import com.mpic.evolution.chair.common.exception.EcmTokenException;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.mpic.evolution.chair.common.returnvo.ErrorEnum.ERR_603;

/**
 * @author by cxd
 * @Classname BaseExceptionHandler
 * @Description TODO
 * @Date 2020/12/16 15:36
 */
@ControllerAdvice
public class BaseExceptionHandler {
    // 捕获参数异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    ResponseDTO  baseExceptionException(Exception e) {
        System.out.println("后台全局异常捕获发现异常");
        e.printStackTrace();
        return ResponseDTO.fail(e.getMessage(),"后台错误",550,550);
    }


    @ExceptionHandler(value = EcmTokenException.class)
    @ResponseBody
    ResponseDTO  tokenException(EcmTokenException e) {
//        System.out.println("后台全局异常捕获发现异常");
//        e.printStackTrace();
        return ResponseDTO.fail(ERR_603.getText(),ERR_603.getText(),Integer.valueOf(ERR_603.getValue()),ERR_603.getValue());
    }
}
