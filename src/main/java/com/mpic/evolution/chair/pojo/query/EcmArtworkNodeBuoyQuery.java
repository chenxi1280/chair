package com.mpic.evolution.chair.pojo.query;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeBuoy;
import lombok.Data;

import java.util.List;

/**
 * @author by cxd
 * @Classname EcmArtworkNodeBuoyQuery
 * @Description TODO
 * @Date 2021/1/12 13:37
 */
@Data
public class EcmArtworkNodeBuoyQuery {
    private Integer fkUserId;
    /**
     * 作品id
     */
    private Integer fkArtworkId;
    private List<EcmArtworkNodeBuoy> ecmArtworkNodeBuoyList;
    private List<Integer> fkNodeIdList;
}
