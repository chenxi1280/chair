package com.mpic.evolution.chair.config.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * creator：cxd
 */
public class ShiroAuthFilter extends FormAuthenticationFilter {

    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     * 这个方法是处理验证登录之前。它的原理就是判断cookie之中是否有登录过的sessionId，如果有，就拿来检测是否真的登录过 了，否则就没有登录过？
     *
     * @param request
     * @param response
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;// 向下转型为HttpServletRequest
        HttpServletResponse httpServletsResponse = (HttpServletResponse) response;// 向下转型HttpServletResponse
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();// 是认证过了
//        if (!authenticated) {// 没有登录
//            if ("OPTIONS".equals(((HttpServletRequest) request).getMethod())) {// 浏览器的跨域请求，预检请求，直接通过
//                return true;// 如果是预检请求，直接应该放行
//            }
//            String token = httpServletRequest.getHeader("authToken");// 头信息出去来
////            if (StringUtils.isEmpty(token)) {// 这里需要相应一下这个请求头
////                token = httpServletRequest.getHeader("sec-websocket-protocol");
////                httpServletResponse.setHeader("sec-websocket-protocol", token);// 设置允许的所有域名来访
////            }
//            if (StringUtils.isEmpty(token)) {// 如果头信息是空，就表示没有登录过
//                httpServletResponse.setCharacterEncoding("UTF-8");// 回传数据编码
//                httpServletResponse.setContentType("application/json");// 设置回传为json格式
//                httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");// 设置允许的所有域名来访
//                // 这个时候我们不需要执行页面跳转，而是需要给前端回传一个json对象，所以需要以上操作,相应ajax请求。
//                // 这个字符串就应该是json字符串。
//                httpServletResponse.getWriter().write(JSONObject.toJSONString(ResponseDTO.fail("您还没有登录!", null, 401, 401)));
//                return false;
//            }
//
//        }
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String requestedWith = httpServletRequest.getHeader("X-Requested-With");// 这句话指的前端是ajax请求

        if (!StringUtils.isEmpty(requestedWith) && "XMLHttpRequest".equals(requestedWith)) {//如果是ajax返回指定格式数据
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("未授权");
        } else {//如果是普通请求进行重定向
            httpServletResponse.sendRedirect("/403");
        }
        return false;
    }
}
