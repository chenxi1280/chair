package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmVideoTemporaryStorage;
import com.mpic.evolution.chair.pojo.query.EcmVideoTemporaryStorageQurey;
import com.mpic.evolution.chair.pojo.vo.EcmVideoTemporaryStorageVO;

/**
 * @author by cxd
 * @Classname EcmVideoStorageService
 * @Description TODO
 * @Date 2020/10/15 9:47
 */
public interface EcmVideoStorageService {
    ResponseDTO videoTemporaryStorage(EcmVideoTemporaryStorageVO ecmVideoTemporaryStorage);

    ResponseDTO updataVideoTemporaryStorage(EcmVideoTemporaryStorageVO ecmVideoTemporaryStorage);

    ResponseDTO getVideoTemporaryStorages(EcmVideoTemporaryStorageQurey ecmVideoTemporaryStorageQurey);

    ResponseDTO delVideoTemporaryStorages(EcmVideoTemporaryStorageQurey ecmVideoTemporaryStorageQurey);
}
