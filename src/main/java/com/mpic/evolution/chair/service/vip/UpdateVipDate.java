package com.mpic.evolution.chair.service.vip;

import com.mpic.evolution.chair.dao.EcmVipPaymentHistoryDao;
import com.mpic.evolution.chair.dao.EcmVipUserInfoDao;
import com.mpic.evolution.chair.pojo.entity.EcmVipPaymentHistory;
import com.mpic.evolution.chair.pojo.entity.EcmVipUserInfo;
import com.mpic.evolution.chair.util.VipDateUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class UpdateVipDate implements PaymentVipService {
    @Resource
    EcmVipUserInfoDao ecmVipUserInfoDao;

    @Resource
    EcmVipPaymentHistoryDao ecmVipPaymentHistoryDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean operationRelateToPayment(Integer number,Integer fkUserId,String type) {
        EcmVipUserInfo ecmVipUserInfo = new EcmVipUserInfo();
        ecmVipUserInfo.setFkUserid(fkUserId);
        ecmVipUserInfo.setFkVipRoleId(1);
        ecmVipUserInfo.setVipStatus((short)1);
        ecmVipUserInfo = ecmVipUserInfoDao.selectByUserInfo(ecmVipUserInfo);
        EcmVipPaymentHistory history = new EcmVipPaymentHistory();
        history.setCreateTime(new Date());
        history.setFkUserid(fkUserId);
        history.setVipType(type);
        if(ecmVipUserInfo != null){
            Date vipEndTime = ecmVipUserInfo.getVipEndTime();
            LocalDateTime endTime = VipDateUtil.formatToLocalDateTime(vipEndTime);
            endTime.plusMonths(number);
            Date date = VipDateUtil.formatToDate(endTime);
            ecmVipUserInfo.setVipEndTime(date);
            ecmVipUserInfo.setUpdateTime(new Date());
            try {
                ecmVipUserInfoDao.updateByPrimaryKeySelective(ecmVipUserInfo);
                ecmVipPaymentHistoryDao.insertSelective(history);
            }catch (Exception e){
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
            return true;
        }else{
            LocalDateTime now = LocalDateTime.now();
            Date sdate = VipDateUtil.formatToDate(now);
            now.plusMonths(number);
            Date edate = VipDateUtil.formatToDate(now);
            ecmVipUserInfo.setVipStartTime(sdate);
            ecmVipUserInfo.setVipEndTime(edate);
            ecmVipUserInfo.setCreateTime(new Date());
            ecmVipUserInfo.setUpdateTime(new Date());
            try {
                ecmVipUserInfoDao.insertSelective(ecmVipUserInfo);
                ecmVipPaymentHistoryDao.insertSelective(history);
            }catch (Exception e){
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
            return true;
        }

    }
}
