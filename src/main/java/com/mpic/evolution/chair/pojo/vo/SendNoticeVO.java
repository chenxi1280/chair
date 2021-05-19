package com.mpic.evolution.chair.pojo.vo;

import lombok.Data;

/**
 * @author by cxd
 * @Classname SendNoticeVO
 * @Description TODO
 * @Date 2021/5/19 18:05
 */
@Data
public class SendNoticeVO {
    /**
     *  模板id
     */
    private String templateId;
    /**
     *  电话号码
     */
    private String phoneNumber;

    private String code;
}
