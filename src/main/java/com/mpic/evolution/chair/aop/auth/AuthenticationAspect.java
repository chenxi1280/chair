package com.mpic.evolution.chair.aop.auth;

import com.mpic.evolution.chair.common.exception.EcmAuthenticationException;
import com.mpic.evolution.chair.common.exception.EcmTokenException;
import com.mpic.evolution.chair.config.annotation.EcmArtworkAuthentication;
import com.mpic.evolution.chair.dao.EcmVipRoleAuthorityDao;
import com.mpic.evolution.chair.dao.EcmVipRoleDao;
import com.mpic.evolution.chair.dao.EcmVipUserInfoDao;
import com.mpic.evolution.chair.pojo.entity.EcmVipRoleAuthority;
import com.mpic.evolution.chair.pojo.entity.EcmVipUserInfo;
import com.mpic.evolution.chair.pojo.vo.EcmVipRoleAuthorityVO;
import com.mpic.evolution.chair.util.JWTUtil;
import com.qcloud.vod.common.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author by cxd
 * @Classname AuthenticationAspect
 * @Description TODO
 * @Date 2021/3/10 20:05
 */
@Aspect
@Component
//spring bean加载优先级注解
@Order(-10)
public class AuthenticationAspect {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationAspect.class);

    final
    EcmVipRoleDao ecmVipRoleDao;
    EcmVipRoleAuthorityDao ecmVipRoleAuthorityDao;
    EcmVipUserInfoDao ecmVipUserInfoDao;


    public AuthenticationAspect(EcmVipRoleDao ecmVipRoleDao, EcmVipRoleAuthorityDao ecmVipRoleAuthorityDao, EcmVipUserInfoDao ecmVipUserInfoDao) {
        this.ecmVipRoleDao = ecmVipRoleDao;
        this.ecmVipRoleAuthorityDao = ecmVipRoleAuthorityDao;
        this.ecmVipUserInfoDao = ecmVipUserInfoDao;
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

        List<EcmVipUserInfo> ecmVipUserInfo = ecmVipUserInfoDao.selectByUserId(Integer.valueOf(userId));
        if (!CollectionUtils.isEmpty(ecmVipUserInfo)){

            int[] role = ecmArtworkAuthentication.role();
            if ( role.length > 0) {
                for (EcmVipUserInfo vipUserInfo : ecmVipUserInfo) {
                    for (int i : role) {
                        if ( i == vipUserInfo.getFkVipRoleId()) {
                            return joinPoint.proceed();
                        }
                    }
                }
            }

            String[] auth = ecmArtworkAuthentication.auth();
            if ( auth.length > 0) {
                List<EcmVipRoleAuthorityVO> ecmVipRoleAuthorities = ecmVipRoleAuthorityDao.selectByEcmVipRoleInfoList(ecmVipUserInfo);
                if (!CollectionUtils.isEmpty(ecmVipRoleAuthorities)) {
                    for (EcmVipRoleAuthority ecmVipRoleAuthority : ecmVipRoleAuthorities) {
                        for (String au : auth) {
                            if (au.equals(ecmVipRoleAuthority.getVipAuthorityDescribe())) {
                                return joinPoint.proceed();
                            }
                        }

                    }
                }

            }

        }

        throw new EcmAuthenticationException();


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


    }

}
