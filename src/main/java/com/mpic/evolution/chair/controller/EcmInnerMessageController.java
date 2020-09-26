package com.mpic.evolution.chair.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.pojo.query.EcmInnerMessageQurey;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.StringUtils;
import com.qcloud.vod.common.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;
import com.mpic.evolution.chair.pojo.vo.EcmInnerMessageVo;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmInnerMessageService;

import lombok.extern.slf4j.Slf4j;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 10:48:54 
*/
@Slf4j
@Controller
@RequestMapping("/EcmInnerMessage")
public class EcmInnerMessageController extends BaseController{
	
	@Resource
	EcmInnerMessageService ecmInnerMessageService;
	

	/**
	 * @param: [user]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/26
	 * 描述 :  web 获取 站内信 接口
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
    @RequestMapping("/getInnerMessage")
    @ResponseBody
	public ResponseDTO getInnerMessage(@RequestBody EcmUserVo user) {
		Integer userIdByHandToken = getUserIdByHandToken();
		if (userIdByHandToken == null){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
		user.setPkUserId(userIdByHandToken);
		return ecmInnerMessageService.getMsg(user);
    }
    
	/**
	 * @param: [ecmInnerMessageQurey]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/26
	 * 描述 : 修改消息为删除 状态接口
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
    @RequestMapping("/batchDelete")
    @ResponseBody
	public ResponseDTO batchDelete(@RequestBody EcmInnerMessageQurey ecmInnerMessageQurey) {
		if (StringUtil.isEmpty(ecmInnerMessageQurey.getToken())){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
		if (CollectionUtils.isEmpty(ecmInnerMessageQurey.getMessageIds()) ){
			return ResponseDTO.ok("无新消息可操作！");
		}
		String userId = JWTUtil.getUserId(ecmInnerMessageQurey.getToken());
		ecmInnerMessageQurey.setPkUserId(Integer.valueOf(userId));
		return ecmInnerMessageService.batchDelete(ecmInnerMessageQurey);
    }
    

    /**
	 * @param: [ecmInnerMessageQurey]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/26
	 * 描述 :  修改消息为已读 状态接口
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
    @RequestMapping("/batchModifyRead")
    @ResponseBody
	public ResponseDTO batchModifyRead(@RequestBody EcmInnerMessageQurey ecmInnerMessageQurey) {
    	if (StringUtil.isEmpty(ecmInnerMessageQurey.getToken())){
    		return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
		String userId = JWTUtil.getUserId(ecmInnerMessageQurey.getToken());
		ecmInnerMessageQurey.setPkUserId(Integer.valueOf(userId));
		return ecmInnerMessageService.batchModifyRead(ecmInnerMessageQurey);

	}
}	
