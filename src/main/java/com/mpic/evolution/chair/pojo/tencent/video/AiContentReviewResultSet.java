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
public class AiContentReviewResultSet {

    private String Type;
    private int Progress;

    private BaseTask PornTask;
    private BaseTask TerrorismTask;
    private BaseTask PoliticalTask;;
    private BaseTask PoliticalOcrTask;
    private BaseTask PornOcrTask;
    private BaseTask PornAsrTask;
    private BaseTask PoliticalAsrTask;





}