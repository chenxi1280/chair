package com.mpic.evolution.chair.pojo.query;

import cn.hutool.db.sql.Query;
import com.mpic.evolution.chair.pojo.entity.EcmUserHistoryFlow;
import com.mpic.evolution.chair.pojo.query.PageQuery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author by cxd
 * @Classname EcmUserHistoryFlowVO
 * @Description TODO
 * @Date 2020/9/7 13:40
 */
@Data
public class EcmUserHistoryFlowQuery extends PageQuery {
    /**
     * 	作者id
     */
    private Integer fkUserid;

    /**
     * 前端传递的参数 用于查询用户一段时间的下行流量使用记录的起始时间
     */
    private Long startDate;

    /**
     * 前端传递的参数 用于查询用户一段时间的下行流量使用记录的截止时间
     */
    private Long endDate;

    /**
     * 作品id
     */
    List<Integer> artworkIds;

}
