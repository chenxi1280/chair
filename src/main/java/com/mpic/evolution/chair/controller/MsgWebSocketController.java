package com.mpic.evolution.chair.controller;


import com.alibaba.fastjson.JSON;
import com.mpic.evolution.chair.config.websocket.WebSocketServer;
import com.mpic.evolution.chair.pojo.vo.EcmInnerMessageVo;
import com.mpic.evolution.chair.util.HttpUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author by cxd
 * @Classname MsgWebSocketController
 * @Description TODO
 * @Date 2020/9/16 10:07
 */
@RestController
@Log4j
public class MsgWebSocketController {

    /**
     * @param: [ecmInnerMessageVOS]
     * @return: org.springframework.http.ResponseEntity<java.lang.String>
     * @author: cxd
     * @Date: 2020/9/26
     * 描述 : webSocket 后台推送接口
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    @RequestMapping("/pushMsg")
    @ResponseBody
    public ResponseEntity<String> pushMsg(@RequestBody List<EcmInnerMessageVo> ecmInnerMessageVOS) {
        System.out.println("发送短信了！");
        if (!CollectionUtils.isEmpty(ecmInnerMessageVOS)) {
            ecmInnerMessageVOS.forEach(v -> {
                try {
                    WebSocketServer.sendInfo(v, String.valueOf(v.getFkUserId()));
                } catch (IOException e) {
                    log.info("WebSocket连接不在本服务器，重新发送");
                    e.printStackTrace();
                    try {
                        HttpUtils.post(HttpUtils.sendHttpsUrl, JSON.toJSONString(ecmInnerMessageVOS));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        log.info("重新发送失败！");
                    }
                }
            });
            return ResponseEntity.ok("MSG SEND SUCCESS");
        }
        return ResponseEntity.ok("MSG SEND ERROR");
    }
}
