package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.vo.ExcelExportVo;

import javax.servlet.http.HttpServletResponse;

public interface ExportExcelService {

     void exportExcel(HttpServletResponse response, ExcelExportVo excelExportVo);

}
