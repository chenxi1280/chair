package com.mpic.evolution.chair.config.exception;

import com.mpic.evolution.chair.common.exception.EcmAuthenticationException;
import com.mpic.evolution.chair.common.exception.EcmTokenException;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.mpic.evolution.chair.common.returnvo.ErrorEnum.ERR_603;

/**
 * @author by cxd
 * @Classname BaseExceptionHandler
 * @Description TODO
 * @Date 2020/12/16 15:36
 */
@Slf4j
@ControllerAdvice
public class BaseExceptionHandler {


    // 捕获参数异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    ResponseDTO  baseExceptionException(Exception e) {
        log.info("后台全局异常捕获发现异常");
        e.printStackTrace();
        return ResponseDTO.fail(e.getMessage(),"后台错误",550,550);
    }


    @ExceptionHandler(value = EcmTokenException.class)
    @ResponseBody
    ResponseDTO  tokenException( EcmTokenException e) {
//        String errorSource=  session.getAttribute("errorSource").toString();
        log.info("token 不合法 ，非法访问");
        e.printStackTrace();
        return ResponseDTO.fail(e.getMessage(),e.getCode());
    }


    @ExceptionHandler(value = EcmAuthenticationException.class)
    @ResponseBody
    ResponseDTO  ecmAuthenticationException(EcmAuthenticationException e) {
        log.info("权限不够！");
        e.printStackTrace();
        return ResponseDTO.fail(e.getMessage(),e.getCode());
    }
}
