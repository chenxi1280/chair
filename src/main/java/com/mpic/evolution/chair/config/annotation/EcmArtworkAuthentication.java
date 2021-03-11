package com.mpic.evolution.chair.config.annotation;

import lombok.NoArgsConstructor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by cxd
 * @Classname EcmArtworkAuthentication
 * @Description TODO
 * @Date 2021/3/10 19:59
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EcmArtworkAuthentication {
    int[] role() default {};
    String[] auth() default {};
}
