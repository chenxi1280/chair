package com.mpic.evolution.chair.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.mpic.evolution.chair.common.constant.SecretKeyConstants;
import com.mpic.evolution.chair.dao.EcmUserDao;
import com.mpic.evolution.chair.dao.EcmUserFlowDao;
import com.mpic.evolution.chair.dao.EcmUserHistoryFlowDao;
import com.mpic.evolution.chair.dao.WxUserDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.entity.EcmUserFlow;
import com.mpic.evolution.chair.pojo.query.EcmUserFlowQuery;
import com.mpic.evolution.chair.pojo.vo.EcmUserFlowVO;
import com.mpic.evolution.chair.pojo.vo.EcmUserHistoryFlowVO;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.util.EncryptUtil;

@Service
public class EcmUserServiceImpl implements EcmUserService {
	
	@Resource
	EcmUserDao ecmUserDao;
	@Resource
	EcmUserFlowDao ecmUserFlowDao;
	@Resource
	EcmUserHistoryFlowDao ecmUserHistoryFlowDao;
	@Resource
	WxUserDao wxUserDao;

	@Override
	public EcmUser getUserInfos(EcmUser record) {
		return ecmUserDao.selectByRecord(record);
	}

	@Override
	public boolean savaUser(EcmUser record) {
		return ecmUserDao.insertSelective(record) < 1 ? false : true;
	}

	@Override
	public void saveToken(EcmUser user, EcmUserVo userVo) {
		ecmUserDao.updateEcmUser(user,userVo);
	}

	@Override
	public boolean updatePwdByToken(EcmUser user, EcmUserVo userVo) {
		int flag = ecmUserDao.updateEcmUser(user, userVo);
		if (flag<0) return false;
		return true;
	}

	@Override
	public void updateEcmUserById(EcmUser ecmUser, EcmUserVo userVo) {
		 ecmUserDao.updateEcmUser(ecmUser, userVo);
	}

	@Override
	public void updateIsvalidByToken(EcmUser user, EcmUserVo userVo) {
		ecmUserDao.updateEcmUser(user,userVo);
		
	}

	@Override
	public ResponseDTO webGetUserInfo(EcmUser ecmUser) {
		EcmUserVo user = ecmUserDao.selectByPkUserId(ecmUser.getPkUserId());
		if (user == null ){
			return ResponseDTO.fail("用户不存在");
		}
		EcmUserFlowVO ecmUserFlow = ecmUserFlowDao.selectByPkUserId(ecmUser.getPkUserId());
		//如果ecmUserFlow 为空，插入用户的新flow信息
		if (ecmUserFlow == null){
			EcmUserFlowVO userFlow= new EcmUserFlowVO();
			userFlow.setUserId(user.getPkUserId());
			userFlow.setUpdateTime( new Date());
			userFlow.setSurplusFlow(500 * 1024);
			userFlow.setTotalFlow(500 * 1024);
			userFlow.setCheckFlow(500 * 1024);
			ecmUserFlowDao.insert(userFlow);
			ecmUserFlow = userFlow;
		}

		user.setPassword(null);
		user.setCardCode(null);
		user.setRoles(null);
		user.setUserLogoStatus(null);
		try {
			user.setMobile(EncryptUtil.aesDecrypt(  user.getMobile(), SecretKeyConstants.secretKey));
		} catch (Exception e) {
			user.setMobile(null);
			e.printStackTrace();
		}

		user.setUserFlow(ecmUserFlow.getSurplusFlow());
		user.setTotalFlow(ecmUserFlow.getTotalFlow());

		return ResponseDTO.ok("成功",user);
	}

	@Override
	public ResponseDTO inspectFlow(EcmUserFlowQuery ecmUserFlowQuery) {
		EcmUserFlowVO userFlow = ecmUserFlowDao.selectByPkUserId(ecmUserFlowQuery.getPkUserId());
		if (Integer.valueOf(ecmUserFlowQuery.getVideoFlow()) > 1024 * 500 ){
			return ResponseDTO.fail("视频大于500M，请减小视频大小");
		}
		if (userFlow.getSurplusFlow() >= Integer.valueOf(ecmUserFlowQuery.getVideoFlow())){
			return ResponseDTO.ok("流量足够，可以上传");
		}
		return ResponseDTO.fail("流量不足，请充值");
	}

	@Override
	@Transactional
	public ResponseDTO reduceFlow(EcmUserHistoryFlowVO ecmUserHistoryFlowVO) {
		ecmUserHistoryFlowVO.setCreatTime(new Date());
		EcmUserFlow userFlow = new EcmUserFlow();
		userFlow.setUserId(ecmUserHistoryFlowVO.getUserId());
		userFlow.setUpdateTime(new Date());
		userFlow.setSurplusFlow(ecmUserHistoryFlowVO.getVideoFlow());
		try {
			ecmUserHistoryFlowDao.insertSelective(ecmUserHistoryFlowVO);
			ecmUserFlowDao.updateReduceFlow(userFlow);
		}catch (Exception e){
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResponseDTO.fail("error");
		}
		return ResponseDTO.ok("success");
	}

	@Override
	public EcmUser getUserInfosByUserId(Integer userIdByHandToken) {
		return ecmUserDao.selectByPrimaryKey(userIdByHandToken);
	}

	@Override
	public boolean updatePwdByUserId(EcmUser user) {
		int flag = ecmUserDao.updateByPrimaryKeySelective(user);
		if (flag<0) return false;
		return true;
	}

}
