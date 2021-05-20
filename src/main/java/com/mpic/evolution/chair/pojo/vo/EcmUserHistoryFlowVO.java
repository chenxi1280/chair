package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmUserHistoryFlow;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author by cxd
 * @Classname EcmUserHistoryFlowVO
 * @Description TODO
 * @Date 2020/9/7 13:40
 */
@Data
public class EcmUserHistoryFlowVO extends EcmUserHistoryFlow {
    /**
     * 前端传递的参数 用于查询用户一段时间的下行流量使用记录的起始时间
     */
    private LocalDateTime startDate;

    /**
     * 前端传递的参数 用于查询用户一段时间的下行流量使用记录的截止时间
     */
    private LocalDateTime endDate;
}
