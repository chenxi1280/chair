package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkEndings;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodePopupSettings;
import lombok.Data;

import java.util.List;

/**
 * @author by cxd
 * @Classname EcmArtworkEndingsVO
 * @Description TODO
 * @Date 2020/12/2 14:22
 */
@Data
public class EcmArtworkEndingsVO extends EcmArtworkEndings {
    private Integer fkUserId;
    private List<Integer> selectTreeList;
    private EcmVideoTemporaryStorageVO videoInfo;

    private EcmArtworkNodePopupSettings ecmArtworkNodePopupSettings;
}
