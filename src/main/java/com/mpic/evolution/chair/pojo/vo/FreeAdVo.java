package com.mpic.evolution.chair.pojo.vo;

import lombok.Data;

@Data
public class FreeAdVo {

    private Integer userId;

    private Integer artworkId;

    private Integer playType;

    private String token;

    /**
     * 1 免广压缩作品
     */
    private Integer videoType;
}
