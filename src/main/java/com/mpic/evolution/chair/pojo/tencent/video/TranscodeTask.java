/**
  * Copyright 2020 bejson.com 
  */
package com.mpic.evolution.chair.pojo.tencent.video;

import lombok.Data;

/**
 * Date: 2020-09-23 17:16:41
 *
 * @author cxd
 */
@Data
public class TranscodeTask {

    private String Status;
    private int ErrCode;
    private String Message;
    private Input Input;
    private Output Output;

}