package com.mpic.evolution.chair.controller;

import com.alibaba.fastjson.JSON;
import com.mpic.evolution.chair.config.websocket.WebSocketServer;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;
import com.mpic.evolution.chair.pojo.vo.EcmInnerMessageVo;
import com.mpic.evolution.chair.service.EcmInnerMessageService;
import com.mpic.evolution.chair.util.StringUtils;
import com.qcloud.vod.common.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author by cxd
 * @Classname MsgWebSocketController
 * @Description TODO
 * @Date 2020/9/16 10:07
 */
@RestController
public class MsgWebSocketController {


    @RequestMapping("/pushMsg")
    public ResponseEntity<String> pushMsg(@RequestBody List<EcmInnerMessageVo> ecmInnerMessageVOS)  {
        if (!CollectionUtils.isEmpty(ecmInnerMessageVOS)) {
            ecmInnerMessageVOS.forEach( v ->  {
                try {
                    WebSocketServer.sendInfo(v, String.valueOf(v.getFkUserId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            return ResponseEntity.ok("MSG SEND SUCCESS");
        }
        return ResponseEntity.ok("MSG SEND error");

    }
}
