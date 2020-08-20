package com.mpic.evolution.chair.service.impl;

import com.csvreader.CsvWriter;
import com.mpic.evolution.chair.dao.BroadcastStatisticsDao;
import com.mpic.evolution.chair.pojo.vo.UserBroadcastInterestedVO;
import com.mpic.evolution.chair.pojo.vo.UserClickStoryLineTimeVideoPartVO;
import com.mpic.evolution.chair.service.BroadcastStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

/**
 *
 * 类名:  BroadcastStatisticsService
 * @author Xuezx
 * @date 2020/6/10 0010 14:53
 * 描述:
 */
@Service
public class BroadcastStatisticsServiceImpl implements BroadcastStatisticsService {

    @Autowired
    BroadcastStatisticsDao dao;

    /**
     *
     * 方法名:  getUserBroadcastInterested
     *
     * @author Xuezx
     * @date 2020/6/10 0010 14:53
     * 描述:
     */
    @Override
    public File getUserBroadcastInterested()  {

        String fileName = UUID.randomUUID().toString();
        String systemName = System.getProperties().getProperty("os.name");
        String folderPath = "/tmp/";
        String windowsType = "Windows";
        if(systemName.contains(windowsType)){
            folderPath = "E:\\";
        }

        String targetFile = folderPath + fileName + ".csv";

        CsvWriter write =new CsvWriter(targetFile,',',Charset.forName("UTF-8"));
        //各字段以引号标记
        write.setForceQualifier(true);

        List<UserBroadcastInterestedVO> daoData = dao.getUserBroadcastInterested();
        List<UserBroadcastInterestedVO> daoLineTitles = dao.getUserBroadcastInterestedLineTitles();
        List<UserBroadcastInterestedVO> rowLineTitles = dao.getUserBroadcastInterestedRowTitles();

        String[] lineTitles = ("\\," + daoLineTitles.get(0).getLineTitles()).split(",");
        String[] rowTitles = rowLineTitles.get(0).getRowTitles().split(",");

        handleToExcel(write, daoData, lineTitles, rowTitles);
        return new File(targetFile);
    }

    @Override
    public File getUserBroadcastEnd() {
        String fileName = UUID.randomUUID().toString();
        String systemName = System.getProperties().getProperty("os.name");
        String folderPath = "/tmp/";
        String windowsType = "Windows";
        if(systemName.contains(windowsType)){
            folderPath = "E:\\";
        }

        String targetFile = folderPath + fileName + ".csv";

        CsvWriter write =new CsvWriter(targetFile,',',Charset.forName("UTF-8"));
        //各字段以引号标记
        write.setForceQualifier(true);

        //SELECT * FROM ecm_statistics_timeline
        List<UserBroadcastInterestedVO> daoData = dao.getUserBroadcastEnd();
        List<UserBroadcastInterestedVO> daoLineTitles = dao.getUserBroadcastInterestedLineTitles();
        List<UserBroadcastInterestedVO> daoRowtitles = dao.getUserBroadcastInterestedRowTitles();

        String[] lineTitles = ("\\," + daoLineTitles.get(0).getLineTitles()).split(",");
        String[] rowTitles = daoRowtitles.get(0).getRowTitles().split(",");

        handleToExcel(write, daoData, lineTitles, rowTitles);
        return new File(targetFile);


    }


    @Override
    public File getUserClickStoryLineTimes() {
        String fileName = UUID.randomUUID().toString();
        String systemName = System.getProperties().getProperty("os.name");
        String folderPath = "/tmp/";
        String windowsType = "Windows";
        if(systemName.contains(windowsType)){
            folderPath = "E:\\";
        }

        String targetFile = folderPath + fileName + ".csv";

        CsvWriter write =new CsvWriter(targetFile,',',Charset.forName("UTF-8"));
        //各字段以引号标记
        write.setForceQualifier(true);

        //SELECT * FROM ecm_statistics_timeline
        List<UserBroadcastInterestedVO> daoData = dao.getUserClickStoryLineTimes();
        List<UserBroadcastInterestedVO> daoLineTitles = dao.getUserBroadcastInterestedLineTitles();
        List<UserBroadcastInterestedVO> daoRowtitles = dao.getUserBroadcastInterestedRowTitles();

        String[] lineTitles = ("\\," + daoLineTitles.get(0).getLineTitles()).split(",");
        String[] rowTitles = daoRowtitles.get(0).getRowTitles().split(",");

        handleToExcel(write, daoData, lineTitles, rowTitles);
        return new File(targetFile);


    }


    @Override
    public File getUserClickStoryLineTimeVideoPart() {
        String fileName = UUID.randomUUID().toString();
        String systemName = System.getProperties().getProperty("os.name");
        String folderPath = "/tmp/";
        String windowsType = "Windows";
        if(systemName.contains(windowsType)){
            folderPath = "E:\\";
        }

        String targetFile = folderPath + fileName + ".csv";

        CsvWriter write =new CsvWriter(targetFile,',',Charset.forName("UTF-8"));
        //各字段以引号标记
        write.setForceQualifier(true);

        //SELECT * FROM ecm_statistics_timeline
        List<UserClickStoryLineTimeVideoPartVO> daoData = dao.getUserClickStoryLineTimeVideoPart();
        try {
        String[] rowTitles = {
                "userId",
                "昵称",
                "作品id",
                "作品名称",
                "视频id",
                "视频播放地址",
                "当时选项",
                "点击故事线还是退出"
        };
            write.writeRecord(rowTitles);
        for (UserClickStoryLineTimeVideoPartVO vo : daoData) {
            String[] lines = {
                    vo.getUserId(),
                    vo.getNickName(),
                    vo.getArtworkId(),
                    vo.getArtworkName(),
                    vo.getVideoId(),
                    vo.getVideoUrl(),
                    vo.getOptions(),
                    vo.getActType()
            };

            write.writeRecord(lines);
        }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        write.close();
    }
        return new File(targetFile);

    }

    private void handleToExcel(CsvWriter write, List<UserBroadcastInterestedVO> daoData, String[] lineTitles, String[] rowTitles) {
        try {
            write.writeRecord(lineTitles);
            for(String s : rowTitles){
                String[] lines = new String[lineTitles.length + 1];
                lines[0] = s;
                String userId = s.split("x♀x")[0];
                for(UserBroadcastInterestedVO vo : daoData){
                    if(userId.equals(vo.getUserId())){
                        String voArtworkId = vo.getArtworkId();
                        for(int i = 0; i < lineTitles.length; i++){
                            String oneLineTitleId = lineTitles[i].split("x♀x")[0];
                            if(voArtworkId.equals(oneLineTitleId)){
                                lines[i] = "1";
                            }
                        }

                    }
                }
                write.writeRecord(lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            write.close();
        }
    }

    @Override
    public void broadcastPageAction(int userId, int videoId, String actionType, int currentPlayTime) {
        dao.broadcastPageAction(userId, videoId, actionType, currentPlayTime);

    }
}
