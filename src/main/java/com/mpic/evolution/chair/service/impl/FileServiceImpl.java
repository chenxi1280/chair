package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.service.FileService;
import com.qcloud.cos.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static com.mpic.evolution.chair.common.constant.CommomConfig.FILE_LINUX_CONFIG_UPLOAD_PATH_TEST;
import static com.mpic.evolution.chair.common.constant.CommomConfig.FILE_WINDOW_CONFIG_UPLOAD_PATH_TEST;

/**
 * @author by cxd
 * @Classname FileServiceImpl
 * @Description TODO
 * @Date 2021/3/1 16:36
 */
@Service
public class FileServiceImpl implements FileService {
    @Override
    public ResponseDTO uploadTxtFile(MultipartFile file) {
        // 获取系统的类别, Window
        String systemType = System.getProperty("os.name");
        String targetFilePath = systemType.toLowerCase().contains("windows") ? FILE_WINDOW_CONFIG_UPLOAD_PATH_TEST: FILE_LINUX_CONFIG_UPLOAD_PATH_TEST;

        String fileName = UUID.randomUUID().toString().replace("-", "");
        File targetFile = new File(targetFilePath + File.separator + fileName + ".txt");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(targetFile);
            IOUtils.copy(file.getInputStream(), fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return  ResponseDTO.fail("网络错误");
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {

            }
        }
        return ResponseDTO.ok();
    }
}
