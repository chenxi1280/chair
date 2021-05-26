package com.mpic.evolution.chair.service;

public interface EcmDownLinkFlowService {
    /**
     * @author SJ
     *	腾讯云查询用户子云点播的已使用下行流量
     */
    long describeCDNStatDetails(String sTime,String eTime,long subAppId);
    /**
     * @author SJ
     *	创建腾讯云子云点播
     */
    Long createSubAppId(String name);
    /**
     * @author SJ
     *  修改子应用状态
     * @param status On：启用。 Off：停用。Destroyed：销毁。
     * @param subAppId
     * @return
     */
    boolean modifySubAppStatus(String status, Long subAppId);
}
