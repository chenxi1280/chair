package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.common.exception.EcmTokenException;
import com.mpic.evolution.chair.service.ExportExcelService;
import com.mpic.evolution.chair.util.JWTUtil;
import com.qcloud.vod.common.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 作者 SJ:
 * @date 创建时间：2021-05-18 17:12:42
 */

@Controller
@RequestMapping("/excel")
public class ExportExcelController {

    @Resource
    ExportExcelService exportExcelService;

    @RequestMapping("/export")
    public void export(String token, HttpServletResponse response){
        Integer userId = this.getUserIdByHandToken(token);
        exportExcelService.exportExcel(response,userId);
    }

    private Integer getUserIdByHandToken(String token){
        if (!StringUtil.isEmpty(token)){
            String userId = JWTUtil.getUserId(token);
            if (!StringUtil.isEmpty(userId)){
                return  Integer.valueOf(userId);
            }
        }
        throw new EcmTokenException(603,"非法访问");
    }
}
