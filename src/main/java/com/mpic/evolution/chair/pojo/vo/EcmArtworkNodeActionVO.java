package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeAction;
import lombok.Data;

/**
 * @author by cxd
 * @Classname EcmArtworkNodeActionVO
 * @Description TODO
 * @Date 2021/4/19 16:18
 */
@Data
public class EcmArtworkNodeActionVO extends EcmArtworkNodeAction {
    private Integer fkUserId;
    /**
     * 作品id
     */
    private Integer fkArtworkId;
}
