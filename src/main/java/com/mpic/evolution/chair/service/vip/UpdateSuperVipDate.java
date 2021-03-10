package com.mpic.evolution.chair.service.vip;

import com.mpic.evolution.chair.dao.EcmVipUserInfoDao;
import com.mpic.evolution.chair.pojo.entity.EcmVipUserInfo;
import com.mpic.evolution.chair.util.VipDateUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class UpdateSuperVipDate implements PaymentVipService {
    @Resource
    EcmVipUserInfoDao ecmVipUserInfoDao;

    @Override
    public boolean operationRelateToPayment(Integer number,Integer fkUserId) {
        EcmVipUserInfo ecmVipUserInfo = new EcmVipUserInfo();
        ecmVipUserInfo.setFkUserid(fkUserId);
        ecmVipUserInfo.setFkVipRoleId(2);
        ecmVipUserInfo.setVipStatus((short)1);
        ecmVipUserInfo = ecmVipUserInfoDao.selectByUserInfo(ecmVipUserInfo);
        if(ecmVipUserInfo != null){
            Date vipEndTime = ecmVipUserInfo.getVipEndTime();
            LocalDateTime endTime = VipDateUtil.formatToLocalDateTime(vipEndTime);
            endTime.plusMonths(number);
            Date date = VipDateUtil.formatToDate(endTime);
            ecmVipUserInfo.setVipEndTime(date);
            ecmVipUserInfo.setUpdateTime(new Date());
            int i = ecmVipUserInfoDao.updateByPrimaryKeySelective(ecmVipUserInfo);
            return i>0?true:false;
        }else{
            LocalDateTime now = LocalDateTime.now();
            Date sdate = VipDateUtil.formatToDate(now);
            now.plusMonths(number);
            Date edate = VipDateUtil.formatToDate(now);
            ecmVipUserInfo.setVipStartTime(sdate);
            ecmVipUserInfo.setVipEndTime(edate);
            ecmVipUserInfo.setCreateTime(new Date());
            ecmVipUserInfo.setUpdateTime(new Date());
            int i = ecmVipUserInfoDao.insertSelective(ecmVipUserInfo);
            return i>0?true:false;
        }
    }
}
