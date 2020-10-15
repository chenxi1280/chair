package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.common.constant.JudgeConstant;
import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.dao.EcmVideoTemporaryStorageDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.entity.EcmVideoTemporaryStorage;
import com.mpic.evolution.chair.pojo.query.EcmVideoTemporaryStorageQurey;
import com.mpic.evolution.chair.pojo.vo.EcmVideoTemporaryStorageVO;
import com.mpic.evolution.chair.service.EcmVideoStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author by cxd
 * @Classname EcmVideoStorageServiceImpl
 * @Description TODO
 * @Date 2020/10/15 9:48
 */
@Service
public class EcmVideoStorageServiceImpl implements EcmVideoStorageService {
    @Resource
    EcmVideoTemporaryStorageDao ecmVideoTemporaryStorageDao;
    @Resource
    EcmArtworkDao ecmArtworkDao;

    @Resource
    EcmArtworkNodesDao ecmArtworkNodesDao;

    @Override
    public ResponseDTO videoTemporaryStorage(EcmVideoTemporaryStorage ecmVideoTemporaryStorage) {
        ecmVideoTemporaryStorage.setVideoStatus((short) 0);
        return ResponseDTO.get(1 == ecmVideoTemporaryStorageDao.insertSelective(ecmVideoTemporaryStorage));
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    @Transactional
    public ResponseDTO updataVideoTemporaryStorage(EcmVideoTemporaryStorage ecmVideoTemporaryStorage) {
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmVideoTemporaryStorage.getFkUserId());
        if (ecmArtwork == null ) {
            ResponseDTO.fail(ErrorEnum.ERR_011.getText());
        }

        if (!ecmArtwork.getFkUserid().equals(ecmVideoTemporaryStorage.getFkUserId())  ) {
            ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        EcmArtworkNodes ecmArtworkNodes = ecmArtworkNodesDao.selectByPrimaryKey(ecmVideoTemporaryStorage.getFkNodeId());
        if (ecmArtworkNodes!=null){
            ResponseDTO.fail(ErrorEnum.ERR_007.getText());
        }
        ecmArtworkNodes.setVideoUrl(ecmVideoTemporaryStorage.getVideoUrl());
        ecmArtworkNodes.setVideoCode(ecmVideoTemporaryStorage.getVideoCode());
        ecmVideoTemporaryStorage.setVideoStatus((short) 1);
        try{
            ecmVideoTemporaryStorageDao.updateByPrimaryKeySelective(ecmVideoTemporaryStorage);
            ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodes);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ResponseDTO.fail(ErrorEnum.ERR_601.getText());
        }

        return ResponseDTO.ok();
    }

    @Override
    public ResponseDTO getVideoTemporaryStorages(EcmVideoTemporaryStorageQurey ecmVideoTemporaryStorageQurey) {
        List<EcmVideoTemporaryStorageVO> vos = ecmVideoTemporaryStorageDao.selectByFkArtworkId(ecmVideoTemporaryStorageQurey.getFkArtworkId());
        for (EcmVideoTemporaryStorageVO vo : vos) {
            if (!vo.getFkUserId().equals(ecmVideoTemporaryStorageQurey.getFkUserId())){
                return  ResponseDTO.fail(ErrorEnum.ERR_603.getText());
            }
        }
        return ResponseDTO.ok(JudgeConstant.SUCCESS,vos);
    }
}
