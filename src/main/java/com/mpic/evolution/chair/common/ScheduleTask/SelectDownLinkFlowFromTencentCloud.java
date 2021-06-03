package com.mpic.evolution.chair.common.ScheduleTask;

import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkFreeAdDao;
import com.mpic.evolution.chair.dao.EcmDownlinkFlowDao;
import com.mpic.evolution.chair.dao.EcmDownlinkFlowHistoryDao;
import com.mpic.evolution.chair.pojo.entity.EcmDownlinkFlowHistory;
import com.mpic.evolution.chair.service.EcmDownLinkFlowService;
import com.mpic.evolution.chair.util.RedisUtil;
import com.mpic.evolution.chair.util.VipDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Component
@Slf4j
public class SelectDownLinkFlowFromTencentCloud {

    @Resource
    private EcmArtworkDao ecmArtworkDao;

    @Resource
    private EcmArtworkFreeAdDao freeAdDao;

    @Resource
    private EcmDownlinkFlowDao downlinkFlowDao;

    @Resource
    private EcmDownlinkFlowHistoryDao historyDao;

    @Resource
    private EcmDownLinkFlowService downLinkFlowService;

    @Resource
    private RedisUtil redisUtil;

    @Scheduled(cron = "0 0 2 * * ?")
    //每天凌晨两点执行
    public void SelectDownLinkFlowFromTencentCloud(){
        //查询所有的免广告作品
        List<Integer> list = freeAdDao.selectAll();
        //去重
        HashSet<Integer> artworkIds = new HashSet<>(list);
        List<Integer> userIds = ecmArtworkDao.selectByArtworkIds(artworkIds);
        List<Integer> subAppIds = downlinkFlowDao.selectByUserIds(userIds);
        LocalDateTime now = LocalDateTime.now();
        //获取当前时间的年月日
        int dayOfMonth = now.getDayOfMonth();
        int year = now.getYear();
        int monthValue = now.getMonthValue();
        String redisKey = "chair-ScheduleTask-SelectDownLinkFlowFromTencentCloud-" + "ScheduleTask";
        if(!redisUtil.hasKey(redisKey)) {
            //多服务节点测试使用 保证定时任务时间只有一个服务会执行此定时任务
            log.info("**********************Scheduled task is executing**************************");
            redisUtil.set(redisKey, "", 300);

            //今天 分两次查询云点播下行流量
            LocalDateTime start = LocalDateTime.of(year, monthValue, dayOfMonth - 2, 0, 0, 0);
            ZonedDateTime startZoned = start.atZone(ZoneId.from(ZoneOffset.UTC));
            start = startZoned.toLocalDateTime();
            start = start.plusHours(-8);
            String startStr = start.toString() + ":00Z";

            LocalDateTime end = LocalDateTime.of(year, monthValue, dayOfMonth - 1, 0, 0, 0);
            ZonedDateTime endZoned = end.atZone(ZoneId.from(ZoneOffset.UTC));
            end = endZoned.toLocalDateTime();
            end = end.plusHours(-8);
            String endStr = end.toString() + ":00Z";

            LocalDateTime end2 = LocalDateTime.of(year, monthValue, dayOfMonth , 0, 0, 0);
            ZonedDateTime end2Zoned = end2.atZone(ZoneId.from(ZoneOffset.UTC));
            end2 = end2Zoned.toLocalDateTime();
            end2 = end2.plusHours(-8);
            String end2Str = end2.toString() + ":00Z";
            for (int i = 0; i < subAppIds.size(); i++) {
                long twoDaysAgoSum = downLinkFlowService.describeCDNStatDetails(startStr, endStr, subAppIds.get(i));
                long yesterdaySum = downLinkFlowService.describeCDNStatDetails(endStr, end2Str, subAppIds.get(i));
                Date twoDaysAgoDate = VipDateUtil.formatToDate(start);
                EcmDownlinkFlowHistory twoDaysHistory = new EcmDownlinkFlowHistory();
                twoDaysHistory.setCreateTime(twoDaysAgoDate);
                EcmDownlinkFlowHistory twoDaysAgoObject = historyDao.selectByRecord(twoDaysHistory);
                if(twoDaysAgoObject == null){
                    twoDaysHistory.setSubAppId(subAppIds.get(i));
                    twoDaysHistory.setFkUserId(userIds.get(i));
                    twoDaysHistory.setStartTime(twoDaysAgoDate);
                    twoDaysHistory.setEndTime(twoDaysAgoDate);
                    twoDaysHistory.setSubFlowStatus(0);
                    twoDaysHistory.setSubUsedFlow(twoDaysAgoSum);
                    historyDao.insertSelective(twoDaysHistory);
                }else{
                    twoDaysAgoObject.setSubUsedFlow(twoDaysAgoSum);
                    historyDao.updateByPrimaryKey(twoDaysAgoObject);
                }
                Date yesterdayDate = VipDateUtil.formatToDate(end);
                EcmDownlinkFlowHistory yesterdayHistory = new EcmDownlinkFlowHistory();
                yesterdayHistory.setCreateTime(yesterdayDate);
                EcmDownlinkFlowHistory yesterdayObject = historyDao.selectByRecord(yesterdayHistory);
                if(yesterdayObject == null){
                    yesterdayHistory.setSubAppId(subAppIds.get(i));
                    yesterdayHistory.setFkUserId(userIds.get(i));
                    yesterdayHistory.setStartTime(yesterdayDate);
                    yesterdayHistory.setEndTime(yesterdayDate);
                    yesterdayHistory.setSubFlowStatus(0);
                    yesterdayHistory.setSubUsedFlow(yesterdaySum);
                    historyDao.insertSelective(yesterdayHistory);
                }else{
                    yesterdayObject.setSubUsedFlow(yesterdaySum);
                    historyDao.updateByPrimaryKey(yesterdayObject);
                }

                twoDaysHistory = null;
                yesterdayHistory = null;
            }

        }

        //多服务节点测试使用 保证定时任务时间只有一个服务会执行此定时任务
        log.info("**********************Scheduled task is not executing**************************");

    }

}
