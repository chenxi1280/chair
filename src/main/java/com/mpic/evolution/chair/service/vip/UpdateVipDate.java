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
import java.util.List;

@Component
public class UpdateVipDate implements PaymentVipService {
    @Resource
    EcmVipUserInfoDao ecmVipUserInfoDao;

    @Resource
    EcmVipPaymentHistoryDao ecmVipPaymentHistoryDao;

    // 在充值会员时需要先判断当前查出的会员信息的截至日期是否在当前日期之前
    // 如果是当前日期之前的说明是重新充值那么此时就不能只修改会员截至时间  会员的起始时间也要修改
    // 如果不是当前日期之前的说明是续费会员充值  此时只需要修改截至时间
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean operationRelateToPayment(Integer number,Integer fkUserId,String type) {
        EcmVipUserInfo ecmVipUserInfo = new EcmVipUserInfo();
        ecmVipUserInfo.setFkUserid(fkUserId);
        ecmVipUserInfo.setFkVipRoleId(1);
        ecmVipUserInfo.setVipStatus((short)1);
        List<EcmVipUserInfo> ecmVipUserInfos = ecmVipUserInfoDao.selectByUserInfo(ecmVipUserInfo);
        EcmVipPaymentHistory history = new EcmVipPaymentHistory();
        history.setCreateTime(new Date());
        history.setFkUserid(fkUserId);
        history.setVipType(type);
        history.setVipMonth(number);
        if(ecmVipUserInfos != null && ecmVipUserInfos.size() > 0){
            ecmVipUserInfo = ecmVipUserInfos.get(0);
            Date vipEndTime = ecmVipUserInfo.getVipEndTime();
            LocalDateTime endTime = VipDateUtil.formatToLocalDateTime(vipEndTime);
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(endTime)){
                //会员过期 再充值情况
                ecmVipUserInfo.setVipStartTime(new Date());
                LocalDateTime localDateTime = now.plusMonths(number);
                Date date = VipDateUtil.formatToDate(localDateTime);
                ecmVipUserInfo.setVipEndTime(date);
                ecmVipUserInfo.setCreateTime(new Date());
                ecmVipUserInfo.setUpdateTime(new Date());
            }else{
                //会员未过期 续费情况
                LocalDateTime localDateTime = endTime.plusMonths(number);
                Date date = VipDateUtil.formatToDate(localDateTime);
                ecmVipUserInfo.setVipEndTime(date);
                ecmVipUserInfo.setUpdateTime(new Date());
            }
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
            LocalDateTime localDateTime = now.plusMonths(number);
            Date edate = VipDateUtil.formatToDate(localDateTime);
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
