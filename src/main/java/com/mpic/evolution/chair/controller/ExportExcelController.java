package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.common.exception.EcmTokenException;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmUserHistoryFlowQuery;
import com.mpic.evolution.chair.pojo.vo.ExcelExportVo;
import com.mpic.evolution.chair.service.ExportExcelService;
import com.mpic.evolution.chair.util.JWTUtil;
import com.qcloud.vod.common.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 作者 SJ:
 * @date 创建时间：2021-05-18 17:12:42
 */

@Controller
@RequestMapping("/excel")
public class ExportExcelController extends BaseController {

    @Resource
    ExportExcelService exportExcelService;

    @ResponseBody
    @RequestMapping("/export")
    public void export(Long endDate, HttpServletResponse response, Long startDate,String token){
        if (!StringUtil.isEmpty(token)){
            String userId = JWTUtil.getUserId(token);
            if (!StringUtil.isEmpty(userId)){
                ExcelExportVo excelExportVo = new ExcelExportVo();
                excelExportVo.setStartDate(startDate);
                excelExportVo.setEndDate(endDate);
                excelExportVo.setUserId(Integer.valueOf(userId));
                exportExcelService.exportExcel(response,excelExportVo);
            }
        }else{
            throw new EcmTokenException(603,"非法访问");
        }

    }

    @ResponseBody
    @RequestMapping("/getDownLinkRecord")
    public ResponseDTO getDownLinkRecord(@RequestBody EcmUserHistoryFlowQuery ecmUserHistoryFlowQuery){
        ecmUserHistoryFlowQuery.setFkUserid(getUserIdByHandToken());
        return exportExcelService.getDownLinkRecord(ecmUserHistoryFlowQuery);
    }

}
