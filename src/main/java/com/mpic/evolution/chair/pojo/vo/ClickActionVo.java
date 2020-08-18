package com.mpic.evolution.chair.pojo.vo;

import lombok.Data;

/**
 * 类名称： ClickActionVo
 *
 * @author: admin
 * @date 2020/8/18 10:03
 * 类描述：
 */
@Data
public class ClickActionVo {

    private int userId;

    private int videoId;

    private int passiveFlag;

    private int currentPlayTime;

    private String actionType;
}
