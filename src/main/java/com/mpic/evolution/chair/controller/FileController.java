package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author by cxd
 * @Classname FileController
 * @Description TODO
 * @Date 2021/3/1 16:34
 */
@Controller
@RequestMapping("/file")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource
    private FileService fileService;

    /**
     * 文件上传测试接口
     * @return
     */
//    @AppIdAuthorization
    @RequestMapping("/uploadTxt")
    public ResponseDTO uploadFileTest(@RequestParam("uploadFile") MultipartFile file) {
        return fileService.uploadTxtFile(file);
    }

}
