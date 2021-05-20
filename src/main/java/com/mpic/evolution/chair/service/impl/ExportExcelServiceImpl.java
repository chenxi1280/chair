package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.common.constant.SecretKeyConstants;
import com.mpic.evolution.chair.common.exception.EcmTokenException;
import com.mpic.evolution.chair.dao.EcmArtworkBroadcastHistoryDao;
import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHistory;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.pojo.vo.ExcelExportVo;
import com.mpic.evolution.chair.service.ExportExcelService;
import com.mpic.evolution.chair.util.EncryptUtil;
import com.mpic.evolution.chair.util.ExpotExcelUtil;
import com.mpic.evolution.chair.util.JWTUtil;
import com.qcloud.vod.common.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 作者 SJ:
 * @date 创建时间：2021-05-18 10:16:31
 */

@Service
public class ExportExcelServiceImpl implements ExportExcelService {

    @Resource
    EcmArtworkBroadcastHistoryDao historyDao;

    @Resource
    EcmArtworkDao ecmArtworkDao;

    @Resource
    EcmArtworkNodesDao ecmArtworkNodesDao;

    @Override
    public void exportExcel(HttpServletResponse response, ExcelExportVo excelExportVo) {
        String userIdStr = JWTUtil.getUserId(excelExportVo.getToken());
        if (StringUtil.isEmpty(userIdStr)){
            throw new EcmTokenException(603,"非法访问");
        }
        Integer userId = Integer.valueOf(userIdStr);
        //excel标题
        String title = "播放记录表";
        //excel表名
        String [] headers = {"序号","用户token","观看作品名称","作品节点xid","进入作品时间"};
        //excel文件名
        String fileName = title + System.currentTimeMillis()+".xls";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDate = dateFormat.format(excelExportVo.getStartDate());
        String eDate =  dateFormat.format(excelExportVo.getEndDate());
        //从数据库中查询出数据组装成content
        //String content[][] = new String[list.size()][5];
        //excel元素
        //TODO 查询userid的全部作品 筛选出免广告的作品id 根据这些id 请求获取这些作品id的播放历史记录 和相关信息
        EcmArtWorkQuery ecmArtWorkQuery = new EcmArtWorkQuery();
        ecmArtWorkQuery.setFkUserid(userId);
        List<EcmArtworkVo> ecmArtworkVos = ecmArtworkDao.selectArtWorks(ecmArtWorkQuery);
        List<EcmArtworkVo> collect = ecmArtworkVos.stream().filter((EcmArtworkVo e) -> e.getPlayType() == 1).collect(Collectors.toList());
        List<EcmArtworkBroadcastHistory> histories = new ArrayList<>();
        List<String> artworkNames = new ArrayList<>();
        EcmArtworkBroadcastHistory history = new EcmArtworkBroadcastHistory();
        collect.forEach( i ->{
                history.setFkArtworkId(i.getPkArtworkId());
                List<EcmArtworkBroadcastHistory> list = historyDao.selectByRecord(history,sDate,sDate);
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
            //TODO 直接从播放记录中获取 作品节点xid
            content[i][3] = histories.get(i).getFkRevolutionId();
            Date startTime = histories.get(i).getStartTime();
            String format = dateFormat.format(startTime);
            content[i][4] = format;
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExpotExcelUtil.getHSSFWorkbook(title, headers, content);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
