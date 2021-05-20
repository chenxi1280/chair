package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.common.exception.EcmTokenException;
import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.ExcelExportVo;
import com.mpic.evolution.chair.service.ExportExcelService;
import com.mpic.evolution.chair.util.JWTUtil;
import com.qcloud.vod.common.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
        HttpServletResponse response =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Integer userId = getUserIdByHandToken();
        if ( userId == null){
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        exportExcelService.exportExcel(response,excelExportVo);
        return ResponseDTO.ok("导出报表成功");
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
