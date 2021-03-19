package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author by cxd
 * @Classname FileService
 * @Description TODO
 * @Date 2021/3/1 16:36
 */
public interface FileService {
    ResponseDTO uploadTxtFile(MultipartFile file);
}
