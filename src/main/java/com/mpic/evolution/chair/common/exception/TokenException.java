package com.mpic.evolution.chair.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by cxd
 * @Classname TokenException
 * @Description TODO
 * @Date 2021/1/8 9:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenException  extends RuntimeException {
        private Integer code;
        private String message;
}
