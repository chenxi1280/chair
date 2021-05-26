package com.mpic.evolution.chair.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ExcelExportVo {
    /**
     * 前端传递的参数 用于查询用户一段时间的下行流量使用记录的截止时间
     */
    private Long endDate;

    /**
     * 前端传递的参数 用于查询用户一段时间的下行流量使用记录的起始时间
     */
    private Long startDate;

    /**
     * 用户身份识别符
     */
    private String token;
    /**
     * 用户id
     */
    private Integer userId;
}
