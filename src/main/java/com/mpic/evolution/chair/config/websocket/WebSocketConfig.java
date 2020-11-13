package com.mpic.evolution.chair.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author by cxd
 * @Classname WebSocketConfig
 * @Description TODO
 * @Date 2020/9/16 9:33
 *      webSocket配置类
 */
@Configuration
public class WebSocketConfig {

    @Bean
    /**
     * @param: []
     * @return: org.springframework.web.socket.server.standard.ServerEndpointExporter
     * @author: cxd
     * @Date: 2020/9/26
     * 描述 :      webSocket 配置方法返回websocketservice对象
     */
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
