/**
  * Copyright 2020 bejson.com 
  */
package com.mpic.evolution.chair.pojo.tencent.video;
import lombok.Data;

import java.util.List;

/**
 * Date: 2020-09-23 17:16:41
 *
 * @author cxd
 */
@Data
public class MetaData {

    private double AudioDuration;
    private List<AudioStreamSet> AudioStreamSet;
    private long Bitrate;
    private String Container;
    private double Duration;
    private int Height;
    private int Rotate;
    private long Size;
    private double VideoDuration;
    private List<VideoStreamSet> VideoStreamSet;
    private int Width;


}