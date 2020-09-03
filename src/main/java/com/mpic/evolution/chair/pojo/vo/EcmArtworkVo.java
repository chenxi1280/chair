package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import lombok.Data;

/**
 * @Classname EcmArtworkVo
 * @Description TODO
 * @Date 2020/8/3 17:58
 * @Created by cxd
 */
@Data
public class EcmArtworkVo extends EcmArtwork {
    
    /**
     * 	调用接口的操作类型
     */
    private String code;
    
    /**
     * 	用户token
     */
    private String token;
    
    /**
     * 	播放次数
     */
    private Integer broadcastCount;
}
