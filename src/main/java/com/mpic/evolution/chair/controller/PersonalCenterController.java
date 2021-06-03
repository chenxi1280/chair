package com.mpic.evolution.chair.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.service.PersonalCenterService;
import com.mpic.evolution.chair.util.AIVerifyUtil;
import com.mpic.evolution.chair.util.MD5Utils;
import com.mpic.evolution.chair.util.StringUtils;
import com.qcloud.vod.common.StringUtil;

import lombok.extern.log4j.Log4j;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-18 15:32:48 
*/

@Controller
@Log4j
@RequestMapping("/personalCenter")
public class PersonalCenterController extends BaseController {
	
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
	@RequestMapping("/saveUserInfo")
	@ResponseBody
	public ResponseDTO savaUserInfo(@RequestBody EcmUserVo ecmUserVo){
		Integer userIdByHandToken = getUserIdByHandToken();
		if (userIdByHandToken == null){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
		ecmUserVo.setPkUserId(userIdByHandToken);
		String username = ecmUserVo.getUsername();
		try {
			if (!StringUtil.isEmpty(username)) {
				EcmUser user = new EcmUser();
				user.setUsername(username);
				user = ecmUserService.getUserInfos(user);
				// 用户昵称是否存在
				if (user != null && !StringUtils.isNullOrBlank(String.valueOf(user.getUsername()))) {
					return ResponseDTO.fail(ErrorEnum.ERR_504.getText(), null, null, ErrorEnum.ERR_504.getValue());
				}
				String result = AIVerifyUtil.convertContent(username);
				if (!StringUtils.isNullOrBlank(result)) {
					return ResponseDTO.fail(ErrorEnum.ERR_510.getText(), result, null, ErrorEnum.ERR_510.getValue());
				}
			}
			return pcService.savaUserInfo(ecmUserVo);
		} catch (MyBatisSystemException e) {
			log.error("账号在数据库中有多条记录");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
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
		EcmUser user = new EcmUser();
		try {
			// 确认密码验证;
			Integer userIdByHandToken = getUserIdByHandToken();
			user.setPkUserId(userIdByHandToken);
			ecmUserVo.setPkUserId(userIdByHandToken);
			EcmUser userInfos = ecmUserService.getUserInfosByUserId(userIdByHandToken);
			//校验原始密码
			if (!userInfos.getPassword().equals(MD5Utils.encrypt(ecmUserVo.getPassword()))) {
				return ResponseDTO.fail(ErrorEnum.ERR_506.getText(), null, null, ErrorEnum.ERR_506.getValue());
			}
			String newPwd = ecmUserVo.getNewPwd();
			if (!newPwd.equals(ecmUserVo.getConfirmPwd())) {
				return ResponseDTO.fail( ErrorEnum.ERR_503.getText(), null, null, ErrorEnum.ERR_503.getValue());
			}
			user.setPassword(MD5Utils.encrypt(ecmUserVo.getNewPwd()));// 修改后的密码以MD5加密入库
			user.setUpdateTime(new Date());// 修改updateTime字段
			boolean flag = ecmUserService.updatePwdByUserId(user);
			if (!flag) {
				return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
			}
			return ResponseDTO.ok("密码修改成功");
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
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
				return ResponseDTO.fail( ErrorEnum.ERR_504.getText(), null, null, ErrorEnum.ERR_504.getValue());
			}
			String result = AIVerifyUtil.convertContent(username);
			if (!StringUtils.isNullOrBlank(result)) {
				return ResponseDTO.fail(ErrorEnum.ERR_510.getText(), result, null,  ErrorEnum.ERR_510.getValue());
			}
			user.setUsername(username);
			user = ecmUserService.getUserInfos(user);
			// 用户昵称是否存在
			if (user != null && !StringUtils.isNullOrBlank(String.valueOf(user.getUsername()))) {
				return ResponseDTO.fail(ErrorEnum.ERR_504.getText(), null, null, ErrorEnum.ERR_504.getValue());
			}
			return ResponseDTO.ok();
		} catch (MyBatisSystemException e) {
			log.error("账号在数据库中有多条记录");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		}
	}
}
