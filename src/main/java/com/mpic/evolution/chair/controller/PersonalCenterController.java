package com.mpic.evolution.chair.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.common.constant.SecretKeyConstants;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.service.PersonalCenterService;
import com.mpic.evolution.chair.util.AIVerifyUtil;
import com.mpic.evolution.chair.util.EncryptUtil;
import com.mpic.evolution.chair.util.MD5Utils;
import com.mpic.evolution.chair.util.StringUtils;

import lombok.extern.log4j.Log4j;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-18 15:32:48 
*/

@Controller
@Log4j
@RequestMapping("/personalCenter")
public class PersonalCenterController {
	
	@Resource
	EcmUserService ecmUserService;
	
	@Resource
	PersonalCenterService pcService;
	
	/**
	 * @param ecmUserVo 
	 * @return com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: sunjie
	 *	保存用户信息
	 * @Date: 2020/9/18
	 */
	@RequestMapping("/savaUserInfo")
	@ResponseBody
	public ResponseDTO savaUserInfo(@RequestBody EcmUserVo ecmUserVo){
		EcmUser user = new EcmUser();
		String username = ecmUserVo.getUsername();
		try {
			if (StringUtils.isNullOrBlank(username)) {
				return ResponseDTO.fail("昵称已被使用", null, null, 504);
			}
			String result = AIVerifyUtil.convertContent(username);
			if (!StringUtils.isNullOrBlank(result)) {
				return ResponseDTO.fail("昵称使用了违禁词汇", result, null, 510);
			}
			user.setUsername(username);
			user = ecmUserService.getUserInfos(user);
			// 用户昵称是否存在
			if (user != null && !StringUtils.isNullOrBlank(String.valueOf(user.getUsername()))) {
				return ResponseDTO.fail("昵称已被使用", null, null, 504);
			}
			return pcService.savaUserInfo(ecmUserVo);
		} catch (MyBatisSystemException e) {
			log.error("账号在数据库中有多条记录");
			e.printStackTrace();
			return ResponseDTO.fail("failed", null, null, "000039");
		}
	}
	
	/**
	 * 忘记密码
	 * @author: sunjie
	 * @param ecmUserVo
	 * 	个人中心用户修改密码
	 * @return com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 *  @Date: 2020/9/18
	 */
	@RequestMapping("/changePwd")
	@ResponseBody
	public ResponseDTO forgetPassword(@RequestBody EcmUserVo ecmUserVo) {
		EcmUserVo userVo = new EcmUserVo();
		EcmUser user = new EcmUser();
		try {
			// 确认密码验证;
			String mobile = ecmUserVo.getMobile();
			String mobileKey = EncryptUtil.aesEncrypt(mobile, SecretKeyConstants.secretKey);
			userVo.setMobile(mobileKey);
			EcmUser userInfos = ecmUserService.getUserInfos(userVo);
			//校验原始密码
			String inputPwd = ecmUserVo.getPassword();
			if (!userInfos.getPassword().equals(inputPwd)) {
				return ResponseDTO.fail("请正确输入原密码", null, null, 506);
			}
			String newPwd = ecmUserVo.getNewPwd();
			if (!newPwd.equals(ecmUserVo.getConfirmPwd())) {
				return ResponseDTO.fail("确认密码错误", null, null, 503);
			}
			user.setPassword(MD5Utils.encrypt(inputPwd));// 修改后的密码以MD5加密入库
			user.setUpdateTime(new Date());// 修改updateTime字段
			boolean flag = ecmUserService.updatePwdByToken(user, userVo);
			if (!flag) {
				return ResponseDTO.fail("failed", null, null, "000039");
			}
			return ResponseDTO.ok("密码修改成功");
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
			return ResponseDTO.fail("failed", null, null, "000039");
		}

	}
	
	/**
	 * 校验昵称
	 * 
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/validateUserName")
	@ResponseBody
	public ResponseDTO validateUsername(@RequestBody EcmUserVo ecmUserVo) {
		EcmUser user = new EcmUser();
		try {
			String username = ecmUserVo.getUsername();
			if (StringUtils.isNullOrBlank(username)) {
				return ResponseDTO.fail("昵称已被使用", null, null, 504);
			}
			String result = AIVerifyUtil.convertContent(username);
			if (!StringUtils.isNullOrBlank(result)) {
				return ResponseDTO.fail("昵称使用了违禁词汇", result, null, 510);
			}
			user.setUsername(username);
			user = ecmUserService.getUserInfos(user);
			// 用户昵称是否存在
			if (user != null && !StringUtils.isNullOrBlank(String.valueOf(user.getUsername()))) {
				return ResponseDTO.fail("昵称已被使用", null, null, 504);
			}
			return ResponseDTO.ok();
		} catch (MyBatisSystemException e) {
			log.error("账号在数据库中有多条记录");
			e.printStackTrace();
			return ResponseDTO.fail("failed", null, null, "000039");
		}
	}
}
