package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_artwork_compression_free
 * @author
 */
@Data
public class EcmArtworkCompressionFree implements Serializable {
    /**
     * 主键
     */
    private Integer pkId;

    /**
     * 用户id
     */
    private Integer fkUserId;

    /**
     * 用户作品id
     */
    private Integer fkArtworkId;

    /**
     * 0免压缩无效 1免压缩有效
     */
    private Integer status;

    /**
     * 记录更新时间
     */
    private Date updateTime;

    /**
     * 记录创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
