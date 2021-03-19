package com.mpic.evolution.chair.aop.auth;

import com.mpic.evolution.chair.common.exception.EcmAuthenticationException;
import com.mpic.evolution.chair.common.exception.EcmTokenException;
import com.mpic.evolution.chair.config.annotation.EcmArtworkAuthentication;
import com.mpic.evolution.chair.dao.EcmVipRoleAuthorityDao;
import com.mpic.evolution.chair.dao.EcmVipUserInfoDao;
import com.mpic.evolution.chair.pojo.entity.EcmVipRoleAuthority;
import com.mpic.evolution.chair.pojo.entity.EcmVipUserInfo;
import com.mpic.evolution.chair.pojo.vo.EcmVipRoleAuthorityVO;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.RedisUtil;
import com.qcloud.vod.common.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author by cxd
 * @Classname AuthenticationAspect
 * @Description TODO
 * @Date 2021/3/10 20:05
 */
@Aspect
@Component
// spring bean加载优先级注解
@Order(-10)
public class AuthenticationAspect {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationAspect.class);


    @Resource
    RedisUtil redisUtil;

    final
    EcmVipRoleAuthorityDao ecmVipRoleAuthorityDao;
    EcmVipUserInfoDao ecmVipUserInfoDao;

    public AuthenticationAspect(EcmVipRoleAuthorityDao ecmVipRoleAuthorityDao, EcmVipUserInfoDao ecmVipUserInfoDao) {
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
        List<EcmVipUserInfo> ecmVipUserInfo =  ecmVipUserInfoDao.selectByUserId(Integer.valueOf(userId),new Date());;
//        if (redisUtil.hasKey(token + "AuthVipUserInfo") ){
//            ecmVipUserInfo = (List<EcmVipUserInfo>) redisUtil.get(token + "AuthVipUserInfo");
//        }else {
//            ecmVipUserInfo = ecmVipUserInfoDao.selectByUserId(Integer.valueOf(userId)) ;
//            redisUtil.set(token+"AuthVipUserInfo",ecmVipUserInfo,60 * 60 * 3);
//        }
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
//                if (redisUtil.hasKey(token + "AuthVipRoleAuthorities") ){
//                    ecmVipRoleAuthorities = (List<EcmVipRoleAuthorityVO>) redisUtil.get(token + "AuthVipRoleAuthorities");
//                }else {
//                    ecmVipRoleAuthorities = ecmVipRoleAuthorityDao.selectByEcmVipRoleInfoList(ecmVipUserInfo);
//                    redisUtil.set(token+"AuthVipRoleAuthorities",ecmVipRoleAuthorities,60 * 60 * 3);
//                }
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
        logger.info("非法访问 ==》 拦截");
        throw new EcmAuthenticationException();


    }

}
