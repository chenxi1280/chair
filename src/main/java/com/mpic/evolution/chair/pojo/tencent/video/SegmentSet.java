/**
  * Copyright 2020 bejson.com 
  */
package com.mpic.evolution.chair.pojo.tencent.video;
import lombok.Data;

import java.util.Date;

/**
 * Date: 2020-09-23 17:16:41
 *
 * @author cxd
**/
@Data
public class SegmentSet {

    private int Confidence;
    private double EndTimeOffset;
    private String Label;
    private Date PicUrlExpireTime;
    private long PicUrlExpireTimeStamp;
    private double StartTimeOffset;
    private String Suggestion;
    private String Url;


}