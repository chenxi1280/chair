package com.mpic.evolution.chair.pojo.query;

import lombok.Data;

/**
 * @author by cxd
 * @Classname EcmVideoTemporaryStorageQurey
 * @Description TODO
 * @Date 2020/10/15 13:23
 */
@Data
public class EcmVideoTemporaryStorageQurey {
    /**
     * 作品id
     */
    private Integer fkArtworkId;

    /**
     * 视频暂存表id
     */
    private Integer pkVideoId;


    /**
     * 用户id
     */
    private Integer fkUserId;
}
