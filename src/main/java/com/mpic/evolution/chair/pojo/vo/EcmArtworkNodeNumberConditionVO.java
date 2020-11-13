package com.mpic.evolution.chair.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeNumberCondition;
import lombok.Data;

/**
 * @author by cxd
 * @Classname EcmArtworkNodeNumberConditionVO
 * @Description TODO
 * @Date 2020/10/20 9:41
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EcmArtworkNodeNumberConditionVO extends EcmArtworkNodeNumberCondition {
    @JsonIgnore
    private int fkUserId;
    private String chosenText;


    private Integer changeConditionValue;

    /**
     * 数值0发生怎样的改变
     */
    private Integer changeConditionValue0;


    /**
     * 数值1发生怎样的改变
     */
    private Integer changeConditionValue1;

    /**
     * 数值2发生怎样的改变
     */
    private Integer changeConditionValue2;


    /**
     * 数值3发生怎样的改变
     */
    private Integer changeConditionValue3;


    /**
     * 数值3发生怎样的改变
     */
    private Boolean saveNameFlag;
}
