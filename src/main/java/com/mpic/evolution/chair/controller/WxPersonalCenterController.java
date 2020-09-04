package com.mpic.evolution.chair.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.service.WxPersonalCenterService;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.RedisUtil;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-4 9:57:20 
*/

@Controller
@RequestMapping("/wxPersonalCenter")
public class WxPersonalCenterController {
	
	@Resource
	EcmUserService ecmUserService;
	
	@Resource
    WxPersonalCenterService personalCenterService;
    
    @Resource
	RedisUtil redisUtil;

	/**
	 * 
	 * @param ecmArtWorkQuery 自带分页
	 * @return com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: sunjie
	 *	个人中心根据用户token获取用户个人信息
	 * @Date: 2020/9/1
	 */
	@RequestMapping("/getWxUserInfo")
	@ResponseBody
	public ResponseDTO getWxUserInfo(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		String token = ecmArtWorkQuery.getToken();
		String userIdStr = JWTUtil.getUserId(token);
		if(StringUtils.isBlank(userIdStr) || !NumberUtils.isParsable(userIdStr)){
    		return ResponseDTO.fail("游客进入");
    	}
		Integer userId = Integer.parseInt(userIdStr);
		EcmUser user = new EcmUser();
		user.setPkUserId(userId);
		user = ecmUserService.getUserInfos(user);
		return ResponseDTO.ok("获取用户信息成功", user);
	}
	

	/**
	 * @
	 * @param ecmArtWorkQuery 自带分页
	 * @return com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: sunjie
	 * 	根据用户的token获取个人中的个人作品
	 * @Date: 2020/9/1
	 */
	@RequestMapping("/getWxUserArtWorks")
	@ResponseBody
	public ResponseDTO getWxUserArtWorks(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		String token = ecmArtWorkQuery.getToken();
		if(StringUtils.isBlank(token)){
    		return ResponseDTO.fail("未登录账号");
    	}
		String userIdStr = JWTUtil.getUserId(token);
		if(StringUtils.isBlank(userIdStr) || !NumberUtils.isParsable(userIdStr)){
    		return ResponseDTO.fail("未登录账号");
    	}
		Integer userId = Integer.parseInt(userIdStr);
    	ecmArtWorkQuery.setFkUserid(userId);
		return personalCenterService.getWxUserArtWorks(ecmArtWorkQuery);
	}
	
}
