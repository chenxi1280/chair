/**
  * Copyright 2020 bejson.com 
  */
package com.mpic.evolution.chair.pojo.tencent.video;

import lombok.Data;

/**
 * Date: 2020-09-23 17:16:41
 *
 * @author cxd
 *
 * 腾讯AI 审核 回调实体类
 */
@Data
public class TencentVideoResult {

    private String EventType;
    private ProcedureStateChangeEvent ProcedureStateChangeEvent;

}