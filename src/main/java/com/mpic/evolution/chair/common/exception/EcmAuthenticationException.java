package com.mpic.evolution.chair.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by cxd
 * @Classname EcmAuthenticationException
 * @Description TODO
 * @Date 2021/3/11 9:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EcmAuthenticationException extends RuntimeException {
    private Integer code;
    private String message;
}
