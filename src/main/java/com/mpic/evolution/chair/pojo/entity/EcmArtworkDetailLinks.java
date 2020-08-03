package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_detail_links
 * @author 
 */
@Data
public class EcmArtworkDetailLinks implements Serializable {
    /**
     * 该表已废弃
     */
    private Integer pkResultId;

    /**
     * 该答案具体对应哪个视频
     */
    private Integer fromFkArtworkDetailId;

    /**
     * 回答之后的结果
     */
    private String answerText;

    /**
     * 选择该答案后，会链接到的下一个视频是哪个
     */
    private Integer toFkArtworkDetailId;

    private static final long serialVersionUID = 1L;
}