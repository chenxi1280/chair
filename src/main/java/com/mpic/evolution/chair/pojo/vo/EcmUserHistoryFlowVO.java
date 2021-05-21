package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmUserHistoryFlow;
import lombok.Data;

import java.time.LocalDateTime;

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
