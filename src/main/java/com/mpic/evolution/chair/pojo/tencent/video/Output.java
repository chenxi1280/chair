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
public class Output {

    private String Url;
    private long Size;
    private String Container;
    private int Height;
    private int Width;
    private long Bitrate;
    private String Md5;
    private double Duration;
    private List<VideoStreamSet> VideoStreamSet;
    private List<AudioStreamSet> AudioStreamSet;
    private long Definition;


}