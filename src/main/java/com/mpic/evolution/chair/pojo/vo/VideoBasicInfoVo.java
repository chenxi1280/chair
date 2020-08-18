package com.mpic.evolution.chair.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * 类名:   VideoBasicInfo
 * @author Xuezx
 * @date 2020/6/12 0012 14:32
 * 描述:视频基础信息类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoBasicInfoVo {

    private int videoId;

    private String chosenText;

    /**
      * 只包含一级孩子，孙子不包括
      */
    private List<VideoBasicInfoVo> sonVideos;

    private String videoUrl;

    private String itemsText;

}
