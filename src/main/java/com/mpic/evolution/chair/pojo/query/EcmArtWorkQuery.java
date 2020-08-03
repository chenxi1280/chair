package com.mpic.evolution.chair.pojo.query;

import lombok.Data;

/**
 * @Classname EcmArtWorkQuery
 * @Description TODO
 * @Date 2020/8/3 17:32
 * @Created by cxd
 */
@Data
public class EcmArtWorkQuery extends PageQuery {

    /**
     * 作者id
     */
    private Integer fkUserid;

    /**
     * 作者名字
     */
    private String artworkName;

    /**
     * 状态，0草稿，1待审核，2已发布审核通过，3审核不通过禁封，5被删除
     */
    private Short artworkStatus;


    /**
     * 四字标签
     */
    private String fourLetterTips;

}
