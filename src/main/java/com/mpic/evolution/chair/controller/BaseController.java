package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.util.JWTUtil;
import com.qcloud.vod.common.StringUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author by cxd
 * @Classname BaseController
 * @Description TODO
 * @Date 2020/8/5 17:42
 */

public class BaseController {

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public HttpSession getRequstSession() {// 获取shiro自己的session
        return getRequest().getSession();
    }

    public Integer getUserIdByHandToken(){
        String token = this.getRequest().getHeader("Authorization");
        if (StringUtil.isEmpty(token)){
            return null;
        }
        String userId = JWTUtil.getUserId(token);
        if (!StringUtil.isEmpty(userId)){
           return  Integer.valueOf(userId);
        }
        return null;

    }

}
