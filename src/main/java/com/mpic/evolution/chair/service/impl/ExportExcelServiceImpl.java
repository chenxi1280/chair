package com.mpic.evolution.chair.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.common.constant.SecretKeyConstants;
import com.mpic.evolution.chair.dao.EcmArtworkBroadcastHistoryDao;
import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkFreeAdDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.pojo.dto.EcmArtworkBroadcastHistoryDTO;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHistory;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.query.EcmUserHistoryFlowQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkFreeAdVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.pojo.vo.ExcelExportVo;
import com.mpic.evolution.chair.service.ExportExcelService;
import com.mpic.evolution.chair.util.ExpotExcelUtil;
import com.mpic.evolution.chair.util.JWTUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 作者 SJ:
 * @date 创建时间：2021-05-18 10:16:31
 */

@Service
public class ExportExcelServiceImpl implements ExportExcelService {

    @Value("${sms.secretId}")
    private String secretId;// 密钥对

    @Value("${sms.secretKey}")
    private String secretKey;// 密钥对

    @Value("${sms.appid}")
    private String appId;// 腾讯短信应用的 SDK AppID

    @Value("${excel.localFilePath}")
    private String localFilePath;

    @Resource
    EcmArtworkBroadcastHistoryDao historyDao;

    @Resource
    EcmArtworkDao ecmArtworkDao;

    @Resource
    EcmArtworkNodesDao ecmArtworkNodesDao;

    @Resource
    EcmArtworkFreeAdDao ecmArtworkFreeAdDao;

    @Override
    public void exportExcel(HttpServletResponse response, ExcelExportVo excelExportVo) {
        Integer userId = excelExportVo.getUserId();
        //excel标题
        String title = "播放记录表";
        //excel表名
        String [] headers = {"序号","用户token","观看作品名称","作品节点xid","进入作品时间"};
        //excel文件名
        String fileName = title + System.currentTimeMillis()+".xls";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Long startDate = excelExportVo.getStartDate();
        Long endDate = excelExportVo.getEndDate();
        String sDate = dateFormat.format(new Date(startDate));
        String eDate =  dateFormat.format(new Date(endDate));
        //excel元素
        // 查询userid的全部作品 筛选出免广告的作品id 根据这些id 请求获取这些作品id的播放历史记录 和相关信息
        EcmArtWorkQuery ecmArtWorkQuery = new EcmArtWorkQuery();
        ecmArtWorkQuery.setFkUserid(userId);
        List<EcmArtworkVo> ecmArtworkVos = ecmArtworkDao.selectArtWorks(ecmArtWorkQuery);

        ArrayList<Integer> artworks = new ArrayList<>();
        ecmArtworkVos.forEach(item ->{
            artworks.add(item.getPkArtworkId());
        });

        List<EcmArtworkFreeAdVO> ecmArtworkFreeAds = ecmArtworkFreeAdDao.selectFreeAdArtwork(artworks);
        List<EcmArtworkBroadcastHistory> histories = new ArrayList<>();
        List<String> artworkNames = new ArrayList<>();
        EcmArtworkBroadcastHistory history = new EcmArtworkBroadcastHistory();
        ecmArtworkFreeAds.forEach( i ->{
                history.setFkArtworkId(i.getFkArtworkId());
                List<EcmArtworkBroadcastHistory> list = historyDao.selectByRecord(history,sDate,eDate);
                list.forEach(j->{
                    artworkNames.add(i.getArtworkName());
                });
                histories.addAll(list);
            }
        );
        String content[][] = new String[histories.size()][5];
        int seriesNum = 0;
        for (int i = 0; i < histories.size(); i++) {
            content[i] = new String[headers.length];
            content[i][0] = String.valueOf(seriesNum++);
            Integer fkUserId = histories.get(i).getFkUserId();
            String token = JWTUtil.sign(String.valueOf(fkUserId), "加密用户",SecretKeyConstants.JWT_SECRET_KEY);
            content[i][1] = token;
            content[i][2] = artworkNames.get(i);
            // 直接从播放记录中获取 作品节点xid
            content[i][3] = histories.get(i).getFkRevolutionId();
            Date startTime = histories.get(i).getStartTime();
            String format = dateFormat.format(startTime);
            content[i][4] = format;
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExpotExcelUtil.getHSSFWorkbook(title, headers, content);
        //检查文件是否存在
        //响应到客户端
        try {

//            this.setResponseHeader(response, fileName);
//            OutputStream os = response.getOutputStream();
            File sourceFile = new File(localFilePath);
            String filePath = localFilePath + fileName;
            FileOutputStream fout = new FileOutputStream(filePath);
            if (!sourceFile.isDirectory() || !sourceFile.exists()) {
                sourceFile.mkdir();
            }
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResponseDTO getDownLinkRecord(EcmUserHistoryFlowQuery ecmUserHistoryFlowQuery) {
        JSONObject data = new JSONObject();
        Integer userId = ecmUserHistoryFlowQuery.getFkUserid();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long startDate = ecmUserHistoryFlowQuery.getStartDate();
        Long endDate = ecmUserHistoryFlowQuery.getEndDate();
        String sDate = dateFormat.format(new Date(startDate));
        String eDate =  dateFormat.format(new Date(endDate));
        EcmArtWorkQuery ecmArtWorkQuery = new EcmArtWorkQuery();
        ecmArtWorkQuery.setFkUserid(userId);
        List<EcmArtworkVo> ecmArtworkVos = ecmArtworkDao.selectArtWorks(ecmArtWorkQuery);

        ArrayList<Integer> artworks = new ArrayList<>();
        ecmArtworkVos.forEach(item ->{
            artworks.add(item.getPkArtworkId());
        });

        List<EcmArtworkFreeAdVO> ecmArtworkFreeAds = ecmArtworkFreeAdDao.selectFreeAdArtwork(artworks);
        ArrayList<Integer> targetArtworks = new ArrayList<>();
        ecmArtworkFreeAds.forEach(item ->{
            targetArtworks.add(item.getFkArtworkId());
        });
        int recordCount = historyDao.selectArtWorkHistoryCount(targetArtworks,sDate,eDate);
        List<EcmArtworkBroadcastHistory> histories = new ArrayList<>();
        List<String> artworkNames = new ArrayList<>();
        EcmUserHistoryFlowQuery query = new EcmUserHistoryFlowQuery();
        query.setPage(ecmUserHistoryFlowQuery.getPage());
        query.setLimit(ecmUserHistoryFlowQuery.getLimit());
        query.setArtworkIds(targetArtworks);
        histories = historyDao.selectByPageQuery(query,sDate,eDate);
        ArrayList<EcmArtworkBroadcastHistoryDTO> broadcastHistories = new ArrayList<>();
        EcmArtworkBroadcastHistoryDTO ecmArtworkBroadcastHistoryDTO = new EcmArtworkBroadcastHistoryDTO();
        for (int i = 0; i < histories.size(); i++) {
            Integer fkArtworkId = histories.get(i).getFkArtworkId();
            EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(fkArtworkId);
            ecmArtworkBroadcastHistoryDTO.setArtworkName(ecmArtwork.getArtworkName());
            Integer fkUserId = histories.get(i).getFkUserId();
            String token = JWTUtil.sign(String.valueOf(fkUserId), "加密用户",SecretKeyConstants.JWT_SECRET_KEY);
            ecmArtworkBroadcastHistoryDTO.setToken(token);
            Date startTime = histories.get(i).getStartTime();
            String format = dateFormat.format(startTime);
            ecmArtworkBroadcastHistoryDTO.setCreateTime(format);
            ecmArtworkBroadcastHistoryDTO.setXid(histories.get(i).getFkRevolutionId());
            broadcastHistories.add(ecmArtworkBroadcastHistoryDTO);
        }
        data.put("count",recordCount);
        data.put("data",broadcastHistories);
        return ResponseDTO.ok(data);
    }

    /**
     * 上传Excel
     * @param fileNewName
     * @return
     */
    private String uploadExcel(String fileNewName){
        // 设置bucket所在的区域，比如广州(gz), 天津(tj)
        Region region = new Region("cq");
        // 初始化客户端配置
        ClientConfig clientConfig = new ClientConfig(region);
        //这里建议设置使用 https 协议
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 初始化秘钥信息
        // 设置用户属性 secretId和SecretKey
        COSCredentials cred = new BasicCOSCredentials(secretId,secretKey);
        // 初始化cosClient
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 上传文件的本地目录

        File localFile = new File(localFilePath);
        // 指定文件将要存放的存储桶
        // 设置要操作的bucket
        String bucketName = "examplebucket-1250000000";
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String key = "excel";
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        System.out.println(putObjectResult);
        return null;
    }

    //发送响应流方法
    private void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
