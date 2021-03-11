package com.mpic.evolution.chair.service.vip;

import com.mpic.evolution.chair.dao.EcmUserExtraflowDao;
import com.mpic.evolution.chair.dao.EcmUserFlowDao;
import com.mpic.evolution.chair.dao.EcmUserHistoryFlowDao;
import com.mpic.evolution.chair.dao.EcmUserVipDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUserExtraflow;
import com.mpic.evolution.chair.pojo.vo.EcmUserFlowVO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class UpdateUploadFlow implements PaymentVipService {

    @Resource
    EcmUserFlowDao ecmUserFlowDao;

    @Resource
    EcmUserExtraflowDao ecmUserExtraflowDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean operationRelateToPayment(Integer number,Integer fkUserId,String type) {
        EcmUserFlowVO ecmUserFlowVO = ecmUserFlowDao.selectByPkUserId(fkUserId);
        ecmUserFlowVO.setPermanentFlow(ecmUserFlowVO.getPermanentFlow()+number);
        EcmUserExtraflow ecmUserExtraflow = new EcmUserExtraflow();
        //插入购买记录
        ecmUserExtraflow.setFkUserId(fkUserId);
        ecmUserExtraflow.setExtraflowCreateTime(new Date());
        ecmUserExtraflow.setExtraflowType(type);
        try {
            ecmUserFlowDao.updateByPrimaryKeySelective(ecmUserFlowVO);
            ecmUserExtraflowDao.insertSelective(ecmUserExtraflow);
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }
}
