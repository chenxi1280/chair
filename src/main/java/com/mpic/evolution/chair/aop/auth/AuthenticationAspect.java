package com.mpic.evolution.chair.aop.auth;

import com.mpic.evolution.chair.common.exception.EcmTokenException;
import com.mpic.evolution.chair.config.annotation.EcmArtworkAuthentication;
import com.mpic.evolution.chair.dao.EcmVipRoleAuthorityDao;
import com.mpic.evolution.chair.dao.EcmVipRoleDao;
import com.mpic.evolution.chair.util.JWTUtil;
import com.qcloud.vod.common.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author by cxd
 * @Classname AuthenticationAspect
 * @Description TODO
 * @Date 2021/3/10 20:05
 */
//@Aspect
//@Component
////spring bean加载优先级注解
//@Order(-10)
public class AuthenticationAspect {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationAspect.class);

    final
    EcmVipRoleDao ecmVipRoleDao;
    EcmVipRoleAuthorityDao ecmVipRoleAuthorityDao;


    public AuthenticationAspect(EcmVipRoleDao ecmVipRoleDao, EcmVipRoleAuthorityDao ecmVipRoleAuthorityDao) {
        this.ecmVipRoleDao = ecmVipRoleDao;
        this.ecmVipRoleAuthorityDao = ecmVipRoleAuthorityDao;
    }

    ThreadLocal<Long> beginTime = new ThreadLocal<Long>();

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(ecmArtworkAuthentication)")
    public void AuthenticationService(EcmArtworkAuthentication ecmArtworkAuthentication) {

    }

    @Around("AuthenticationService(ecmArtworkAuthentication)")
    public Object doAround(ProceedingJoinPoint joinPoint, EcmArtworkAuthentication ecmArtworkAuthentication) throws Throwable {
        beginTime.set(System.currentTimeMillis());

        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        String token= request.getHeader("Authorization");
        if (StringUtil.isEmpty(token)){
            throw new EcmTokenException(603,"非法访问");
        }
        String userId = JWTUtil.getUserId(token);
        if (StringUtil.isEmpty(userId)){
            throw new EcmTokenException(603,"非法访问");
        }






        int[] role = ecmArtworkAuthentication.role();


//        String card = null;
//        List<Long> roleList = new ArrayList<>();
//        for (Object arg : joinPoint.getArgs()) {
//            if (arg != null && arg.getClass() == RequestFacade.class) {
//                RequestFacade request = (RequestFacade) arg;
////                card = CookieUtils.getCardFromCookie(request);
////                if (StringUtils.isEmpty(card)) {
////                    return JsonResult.buildFailResult(-1, 1000, "权限验证未通过", null);
////                }
////                List<Role> roles = authDao.getRolesByCard(card);
////                for (Role role : roles) {
////                    roleList.add(role.getId());
////                }
//                break;
//            }
//        }
//        logger.info("[authentication] user={}, roles={}", card, roleList);
//        long[] aims = authentication.role();
//        boolean isPass = false;
//        for (long aim : aims) {
//            if (roleList.contains(aim)) {
//                isPass = true;
//            }
//        }
//        if (isPass) {
//            logger.info("[authentication] authentication pass, cost time: {}", System.currentTimeMillis() - beginTime.get());
//            return joinPoint.proceed();
//        } else {
//            beginTime.set(System.currentTimeMillis());
//            logger.info("[authentication] authentication reject, cost time: {}", System.currentTimeMillis() - beginTime.get());
////            return JsonResult.buildFailResult(-1, 1000, "权限验证未通过", null);
//        }


        return joinPoint.proceed();
    }

}
