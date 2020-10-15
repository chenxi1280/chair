package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmVideoTemporaryStorage;
import com.mpic.evolution.chair.pojo.query.EcmVideoTemporaryStorageQurey;

/**
 * @author by cxd
 * @Classname EcmVideoStorageService
 * @Description TODO
 * @Date 2020/10/15 9:47
 */
public interface EcmVideoStorageService {
    ResponseDTO videoTemporaryStorage(EcmVideoTemporaryStorage ecmVideoTemporaryStorage);

    ResponseDTO updataVideoTemporaryStorage(EcmVideoTemporaryStorage ecmVideoTemporaryStorage);

    ResponseDTO getVideoTemporaryStorages(EcmVideoTemporaryStorageQurey ecmVideoTemporaryStorageQurey);
}
