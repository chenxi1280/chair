package com.mpic.evolution.chair.pojo.vo;

import lombok.Data;

/**
 * @author by cxd
 * @Classname NodeNumberConditionVO
 * @Description TODO
 * @Date 2020/10/20 13:32
 */
@Data
public class NodeNumberConditionVO {
    private String change;

    private String changeValue;

    /**
     * 前端最小值
     */
    private String appearValueMin;

    /**
     * 前端最大值
     */
    private String appearValueMax;

    /**
     * 是否启用改变数值
     */
    private Byte changeFlag;

    /**
     * 是否启用判断出现数值
     */
    private Byte appearFlag;

    /**
     * 是否启用判断出现名字
     */
    private Byte nameFlag;

    /**
     * 是否启用判断出现名字
     */
    private Byte nameDisplay;

    /**
     *  文字选项设置
     */
    private String nameCondition;
}
