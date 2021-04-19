package com.mpic.evolution.chair.pojo.query;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeAction;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeBuoy;
import lombok.Data;

import java.util.List;

/**
 * @author by cxd
 * @Classname EcmArtworkNodeActionQuery
 * @Description TODO
 * @Date 2021/4/19 16:17
 */
@Data
public class EcmArtworkNodeActionQuery {

    private Integer fkUserId;
    /**
     * 作品id
     */
    private Integer fkArtworkId;

    private List<EcmArtworkNodeAction> ecmArtworkNodeActionList;

    private List<Integer> fkNodeIdList;

}
