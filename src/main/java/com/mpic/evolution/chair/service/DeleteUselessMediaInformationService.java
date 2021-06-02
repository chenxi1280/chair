package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;

public interface DeleteUselessMediaInformationService {
    /**
     * @author SJ
     * 删除无用的云点播文件信息
     * @return ResponseDTO
     */
    ResponseDTO deleteUselessMediaInformation();
}
