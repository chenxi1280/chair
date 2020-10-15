package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmVideoTemporaryStorage;
import com.mpic.evolution.chair.pojo.query.EcmVideoTemporaryStorageQurey;
import com.mpic.evolution.chair.service.EcmVideoStorageService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author by cxd
 * @Classname EcmVideoStorageController
 * @Description TODO
 * @Date 2020/10/15 9:41
 */
@RestController
@RequestMapping("/EcmVideoStorage")
public class EcmVideoStorageController extends BaseController {
    @Resource
    EcmVideoStorageService ecmVideoStorageService;

    /**
     * @param: [ecmVideoTemporaryStorage] 请求头中带token
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/10/15
     * 描述 : 暂存上传视频接口
     *       成功: status 200  msg "success”   成功保存
     *       失败: status 500  msg "error“
     */
    @RequestMapping("/videoTemporaryStorage")
    ResponseDTO videoTemporaryStorage(@RequestBody EcmVideoTemporaryStorage ecmVideoTemporaryStorage) {
        Integer userIdByHandToken = getUserIdByHandToken();
        if (userIdByHandToken == null){
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        ecmVideoTemporaryStorage.setFkUserId(userIdByHandToken);
        return ecmVideoStorageService.videoTemporaryStorage(ecmVideoTemporaryStorage);
    }

    /**
     * @param: [ecmVideoTemporaryStorage]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/10/15
     * 描述 : 拖动保存视频接口
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    @RequestMapping("/updataVideoTemporaryStorage")
    ResponseDTO updataVideoTemporaryStorage(@RequestBody EcmVideoTemporaryStorage ecmVideoTemporaryStorage) {
        Integer userIdByHandToken = getUserIdByHandToken();
        if (userIdByHandToken == null){
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        ecmVideoTemporaryStorage.setFkUserId(userIdByHandToken);
        return ecmVideoStorageService.updataVideoTemporaryStorage(ecmVideoTemporaryStorage);
    }

    /**
     * @param: [ecmVideoTemporaryStorageQurey]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/10/15
     * 描述 : 通过作品id，获取暂存视频
     *       成功: status 200  msg "success”   date: List<EcmVideoTemporaryStorage>
     *       失败: status 500  msg "error“
     */
    @RequestMapping("/getVideoTemporaryStorages")
    ResponseDTO getVideoTemporaryStorages(@RequestBody EcmVideoTemporaryStorageQurey ecmVideoTemporaryStorageQurey) {
        Integer userIdByHandToken = getUserIdByHandToken();
        if (userIdByHandToken == null){
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        ecmVideoTemporaryStorageQurey.setFkUserId(userIdByHandToken);
        return ecmVideoStorageService.getVideoTemporaryStorages(ecmVideoTemporaryStorageQurey);
    }

}
