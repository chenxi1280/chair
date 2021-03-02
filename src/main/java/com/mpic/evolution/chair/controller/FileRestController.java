package com.mpic.evolution.chair.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static com.mpic.evolution.chair.common.constant.CommomConfig.FILE_LINUX_CONFIG_UPLOAD_PATH_TEST;
import static com.mpic.evolution.chair.common.constant.CommomConfig.FILE_WINDOW_CONFIG_UPLOAD_PATH_TEST;

/**
 * @author by cxd
 * @Classname FileRestController
 * @Description TODO
 * @Date 2021/3/2 10:06
 */
@RestController
public class FileRestController {


    @RequestMapping(value = "/file/{file}")
    public void getFileTxt(@PathVariable String file, HttpServletRequest request, HttpServletResponse response){
        // 获取系统的类别, Window
        String systemType = System.getProperty("os.name");

        String targetFilePath = systemType.toLowerCase().contains("windows") ? FILE_WINDOW_CONFIG_UPLOAD_PATH_TEST: FILE_LINUX_CONFIG_UPLOAD_PATH_TEST;

        String fullPath = targetFilePath +"/"+ file ;
        try {
            InputStream myStream = new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File downloadFile = new File(fullPath);

        ServletContext context = request.getServletContext();

        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
            System.out.println("context getMimeType is null");
        }
        System.out.println("MIME type: " + mimeType);

        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // Copy the stream to the response's output stream.
        try {
            InputStream myStream = new FileInputStream(fullPath);
            IOUtils.copy(myStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
