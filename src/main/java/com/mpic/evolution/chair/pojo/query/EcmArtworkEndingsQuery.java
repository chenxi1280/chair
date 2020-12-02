package com.mpic.evolution.chair.pojo.query;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkEndings;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkEndingsVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author by cxd
 * @Classname EcmArtworkEndingsQuery
 * @Description TODO
 * @Date 2020/12/2 14:58
 */
@Data
public class EcmArtworkEndingsQuery {
    private int fkUserId;
    private int fkArtworkId;
    private String ecmArtworkEndingsVOSJson;
    private List<EcmArtworkEndingsVO> ecmArtworkEndingsVOS;
}
