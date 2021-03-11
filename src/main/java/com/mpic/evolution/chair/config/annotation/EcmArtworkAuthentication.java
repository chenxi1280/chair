package com.mpic.evolution.chair.config.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by cxd
 * @Classname EcmArtworkAuthentication
 * @Description TODO
 * @Date 2021/3/10 19:59
 *
 *       会员权限注解 （方法上使用）
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EcmArtworkAuthentication {
    /**
     * 对应的角色
     */
    int[] role() default {};
    /**
     * 对应的权限
     */
    String[] auth() default {};
}
