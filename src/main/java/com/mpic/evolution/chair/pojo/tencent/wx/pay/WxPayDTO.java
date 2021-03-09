package com.mpic.evolution.chair.pojo.tencent.wx.pay;

import lombok.Data;

import java.util.Date;

/**
 * @author by cxd
 * @Classname WxPayDTO
 * @Description TODO
 * @Date 2021/3/9 17:19
 */
@Data
public class WxPayDTO {

    private String id;
    private Date create_time;
    private String resource_type;
    private String event_type;
    private WxPayResourceDTO resource;
    private String summary;
}
