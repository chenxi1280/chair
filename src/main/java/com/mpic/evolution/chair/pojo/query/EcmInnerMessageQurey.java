package com.mpic.evolution.chair.pojo.query;

import lombok.Data;

import java.util.List;

/**
 * @author by cxd
 * @Classname EcmInnerMessageQurey
 * @Description TODO
 * @Date 2020/8/28 10:42
 */
@Data
public class EcmInnerMessageQurey {

    private Integer pkUserId;

    private String token;//用户的身份识别符
    /**
     * 信息记录id
     */
    private Integer pkMessageId;

    /**
     * 	前端传回的信息id list
     */
    private List<Integer> messageIds;


}
