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
}
