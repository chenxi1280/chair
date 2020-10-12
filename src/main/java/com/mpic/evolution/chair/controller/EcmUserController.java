package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.query.EcmUserFlowQuery;
import com.mpic.evolution.chair.pojo.vo.EcmUserHistoryFlowVO;
import com.mpic.evolution.chair.service.EcmUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author by cxd
 * @Classname EcmUserController
 * @Description TODO
 * @Date 2020/9/7 9:21
 */
@Controller
@RequestMapping("user")
public class EcmUserController extends BaseController{

    @Resource
    EcmUserService ecmUserService;

    /**
     * @param: [EcmUser]  请求头中必须带有token
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/9/7
     * 描述 :  调用请求头中的 token获取 userId 查询用户信息和流量信息
     *       成功: status 200  msg "success”  data: ecmUserVo信息
     *       失败: status 500  msg "error"
     */
       @ResponseBody
    @RequestMapping("webGetUserInfo")
    ResponseDTO webGetUserInfo(){
           EcmUser ecmUser = new EcmUser();

           Integer userId = getUserIdByHandToken();
           if ( userId == null){
               return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
           }
           ecmUser.setPkUserId(Integer.valueOf(userId));

        return ecmUserService.webGetUserInfo(ecmUser);
    }

    /**
     * @param: [ecmUserFlowQuery] 请求头中必须带有token
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/9/7
     * 描述 :   查询流量是否足够上传当前视频
     *       保存成功: status 200  msg "流量足够”
     *       保存失败: status 500  msg "流量不足“
     */
    @ResponseBody
    @RequestMapping("inspectFlow")
    ResponseDTO inspectFlow(@RequestBody EcmUserFlowQuery ecmUserFlowQuery){
        Integer userId = getUserIdByHandToken();
        if ( userId == null){
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        ecmUserFlowQuery.setPkUserId(Integer.valueOf(userId));
        return ecmUserService.inspectFlow(ecmUserFlowQuery);
    }


    /**
     * @param: [ecmUserHistoryFlowVO]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/9/26
     * 描述 : 减去当前当前上传视频的流量大小
     *       成功: status 200  msg "success”
     *       失败: status 500  msg "error“
     */
    @ResponseBody
    @RequestMapping("reduceFlow")
    ResponseDTO reduceFlow(@RequestBody EcmUserHistoryFlowVO ecmUserHistoryFlowVO){
        Integer userId = getUserIdByHandToken();
        if ( userId == null){
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        ecmUserHistoryFlowVO.setUserId(userId);
        return ecmUserService.reduceFlow(ecmUserHistoryFlowVO);
    }



}
