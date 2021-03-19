package com.mpic.evolution.chair.config.interceptor;

import com.mpic.evolution.chair.common.exception.EcmTokenException;
import com.mpic.evolution.chair.util.JWTUtil;
import com.qcloud.vod.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author by cxd
 * @Classname UserAccessInterceptor
 * @Description TODO
 * @Date 2021/3/10 18:08
 */
@Component
public class UserAccessInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);

    /**
     * 在请求到达Controller控制器之前 通过拦截器执行一段代码
     * 如果方法返回true,继续执行后续操作
     * 如果返回false，执行中断请求处理，请求不会发送到Controller
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        logger.info("拦截器  在控制器执行之前执行");

        String token = request.getHeader("Authorization");
        if (StringUtil.isEmpty(token)){
            logger.info("拦截器 -- > 非法访问");
            throw new EcmTokenException();
        }
        if (StringUtil.isEmpty(JWTUtil.getUserId(token))){
            logger.info("拦截器 -- > 非法访问");
            throw new EcmTokenException();
        }

        return true;
    }

    /**
     * 控制器之后，跳转前
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        logger.info("拦截器  在控制器执行之后执行");
    }

    /**
     * 跳转之后执行
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        logger.info("拦截器  最后执行");
    }
}
