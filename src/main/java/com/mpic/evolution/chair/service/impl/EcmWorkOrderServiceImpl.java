package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.dao.EcmWorkOrderDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmWorkOrderVO;
import com.mpic.evolution.chair.service.EcmWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.mpic.evolution.chair.common.constant.CommonField.INT_THREE;
import static com.mpic.evolution.chair.common.returnvo.ErrorEnum.ERR_613;

/**
 * @author by cxd
 * @Classname EcmWorkOrderServiceImpl
 * @Description TODO
 * @Date 2021/5/31 13:26
 */
@Service
public class EcmWorkOrderServiceImpl implements EcmWorkOrderService {

    final
    EcmWorkOrderDao ecmWorkOrderDao;

    public EcmWorkOrderServiceImpl(EcmWorkOrderDao ecmWorkOrderDao) {
        this.ecmWorkOrderDao = ecmWorkOrderDao;
    }

    @Override
    public ResponseDTO saveWorkOrder(EcmWorkOrderVO ecmWorkOrderVO) {
        Integer count = ecmWorkOrderDao.selectCountByUserId(ecmWorkOrderVO.getFkUserId());

        if ( count >= INT_THREE ){
            return ResponseDTO.fail(ERR_613.getText(),ERR_613.getValue());
        }
        ecmWorkOrderVO.setCreateTime(new Date());
        ecmWorkOrderVO.setApplyStatus(0);
        ecmWorkOrderVO.setUserApplicationFunction("下行流量");
        ecmWorkOrderDao.insertSelective(ecmWorkOrderVO);

        return ResponseDTO.ok();
    }
}
