package com.mpic.evolution.chair.service;

import javax.servlet.http.HttpServletResponse;

public interface ExportExcelService {

     void exportExcel(HttpServletResponse response,Integer userId);

}
