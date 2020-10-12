package com.mpic.evolution.chair.pojo.query;

import lombok.Data;

/**
 * @author by cxd
 * @Classname EcmUserFlowQuery
 * @Description TODO
 * @Date 2020/9/7 13:16
 */
@Data
public class EcmUserFlowQuery {
	
    private Integer pkUserId;
    
    /**
     * 	上传视频的大小
     */
    private String videoFlow;
}
