package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * statistic_storyline_use_history
 * @author
 */
@Data
public class StatisticStorylineUseHistory implements Serializable {
    /**
     * 记录故事线使用id
     */
    private Integer statisticStorylineUseHistoryId;

    /**
     * 用户id
     */
    private Integer fkUserId;

    /**
     * 作品id
     */
    private Integer fkArtworkId;

    /**
     * 节点id
     */
    private Integer fkNodeId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
