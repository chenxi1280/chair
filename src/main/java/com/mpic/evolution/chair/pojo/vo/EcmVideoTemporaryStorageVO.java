package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmVideoTemporaryStorage;
import lombok.Data;

/**
 * @author by cxd
 * @Classname EcmVideoTemporaryStorageVO
 * @Description TODO
 * @Date 2020/10/15 13:25
 */
@Data
public class EcmVideoTemporaryStorageVO  extends EcmVideoTemporaryStorage {

    private String videoHigh;

    private String videoWidth;

    private String videoTime;
}
