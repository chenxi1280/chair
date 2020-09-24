package com.mpic.evolution.chair.pojo.tencent.video;

import lombok.Data;

/**
 * @author by cxd
 * @Classname BaceTask
 * @Description TODO
 * @Date 2020/9/24 10:21
 */
@Data
public class BaseTask {
    private int ErrCode;
    private String Message;
    private String Status;
    private int Progress;
    private Input Input;
    private Output Output;
}
