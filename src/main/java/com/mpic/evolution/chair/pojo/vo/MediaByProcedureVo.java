package com.mpic.evolution.chair.pojo.vo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类名称： MediaByProcedureVo
 *
 * @author: admin
 * @date 2020/8/19 14:15
 * 类描述：
 */
@Data
public class MediaByProcedureVo {

    /**腾讯云内部的视频id*/
    private String videoCode;

    private Integer pipelineId;

    private Integer artworkId;

    private Integer videoId;

    /**
      * 是否审核完成，0是未完成，1是完成
      */
    private Integer finishedFlag;

    private LocalDateTime pipelineTime;

    /**0通过审核，1不通过审核，2视频失效，3其他原因*/
    private Integer pipelineResult;

    private String resultDetails;


}
