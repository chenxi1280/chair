package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmUserHistoryFlowQuery;
import com.mpic.evolution.chair.pojo.vo.ExcelExportVo;

import javax.servlet.http.HttpServletResponse;

public interface ExportExcelService {

     void exportExcel(HttpServletResponse response, ExcelExportVo excelExportVo);

    ResponseDTO getDownLinkRecord(EcmUserHistoryFlowQuery ecmUserHistoryFlowQuery);
}
