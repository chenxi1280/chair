package com.mpic.evolution.chair.controller.file;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author by cxd
 * @Classname FileController
 * @Description TODO
 * @Date 2021/3/1 16:34
 */
@Controller
@RequestMapping("file")
public class FileController {
    final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 文件上传测试接口
     * @return
     */
    @RequestMapping("uploadTxt")
    @ResponseBody
    public ResponseDTO uploadFileTest(@RequestParam("uploadFile") MultipartFile file) {
        return fileService.uploadTxtFile(file);
    }

}
