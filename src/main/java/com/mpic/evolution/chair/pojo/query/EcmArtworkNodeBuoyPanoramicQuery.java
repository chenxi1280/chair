package com.mpic.evolution.chair.pojo.query;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeBuoy;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeBuoyPanoramic;
import lombok.Data;

import java.util.List;

/**
 * @author by cxd
 * @Classname EcmArtworkNodeBuoyPanoramicQuery
 * @Description TODO
 * @Date 2021/5/13 17:28
 */
@Data
public class EcmArtworkNodeBuoyPanoramicQuery {

    private Integer fkUserId;
    /**
     * 作品id
     */
    private Integer fkArtworkId;
    private List<EcmArtworkNodeBuoyPanoramic> ecmArtworkNodeBuoyList;

    private List<Integer> fkNodeIdList;

    /**
     * 播放到结尾的时候 重播还是 自动选择 某一个
     */
    private Integer buoyPlayEndType;


}
