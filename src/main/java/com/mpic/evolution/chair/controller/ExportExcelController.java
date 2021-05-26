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
    public ResponseDTO export(@RequestBody ExcelExportVo excelExportVo){
        excelExportVo.setUserId(Integer.valueOf(getUserIdByHandToken()));
        return exportExcelService.exportExcel(excelExportVo);
    }

    @ResponseBody
    @RequestMapping("/getDownLinkRecord")
    public ResponseDTO getDownLinkRecord(@RequestBody EcmUserHistoryFlowQuery ecmUserHistoryFlowQuery){
        ecmUserHistoryFlowQuery.setFkUserid(getUserIdByHandToken());
        return exportExcelService.getDownLinkRecord(ecmUserHistoryFlowQuery);
    }

}
