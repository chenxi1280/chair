package com.mpic.evolution.chair.service.impl;

import static com.mpic.evolution.chair.common.constant.JudgeConstant.SUCCESS;
import static com.mpic.evolution.chair.common.constant.JudgeConstant.flowMax;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.mpic.evolution.chair.common.constant.SecretKeyConstants;
import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.dao.EcmUserDao;
import com.mpic.evolution.chair.dao.EcmUserExtraflowDao;
import com.mpic.evolution.chair.dao.EcmUserFlowDao;
import com.mpic.evolution.chair.dao.EcmUserHistoryFlowDao;
import com.mpic.evolution.chair.dao.EcmUserVipDao;
import com.mpic.evolution.chair.dao.WxUserDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.entity.EcmUserExtraflow;
import com.mpic.evolution.chair.pojo.entity.EcmUserFlow;
import com.mpic.evolution.chair.pojo.entity.EcmUserVip;
import com.mpic.evolution.chair.pojo.query.EcmUserExtraflowQuery;
import com.mpic.evolution.chair.pojo.query.EcmUserFlowQuery;
import com.mpic.evolution.chair.pojo.query.EcmUserVipQuery;
import com.mpic.evolution.chair.pojo.vo.EcmUserFlowVO;
import com.mpic.evolution.chair.pojo.vo.EcmUserHistoryFlowVO;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.util.EncryptUtil;

/**
 * @author Administrator
 */
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
	@Resource
    EcmUserVipDao ecmUserVipDao;
	@Resource
    EcmUserExtraflowDao ecmUserExtraflowDao;

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
	
	/**
	 * @param: [ecmUser] 用户身份信息
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: SJ
     * @Date: 2020/10/13
     * 	描述 :   流量的查询
	 */
	@Override
	public ResponseDTO webGetUserInfo(EcmUser ecmUser) {
		int totalflow = 500 * 1024;
		EcmUserVo user = ecmUserDao.selectByPkUserId(ecmUser.getPkUserId());
		if (user == null ){
			return ResponseDTO.fail(ErrorEnum.ERR_003.getText());
		}
		EcmUserFlowVO ecmUserFlow = ecmUserFlowDao.selectByPkUserId(ecmUser.getPkUserId());
		//如果ecmUserFlow 为空，插入用户的新flow信息
		if (ecmUserFlow == null){
			EcmUserFlowVO userFlow= new EcmUserFlowVO();
			userFlow.setUserId(user.getPkUserId());
			userFlow.setUpdateTime( new Date());
			userFlow.setSurplusFlow(totalflow);
			userFlow.setTotalFlow(totalflow);
			userFlow.setCheckFlow(totalflow);
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
		//查询本次请求的时间点用户是否还有未使用的流量包
		EcmUserExtraflowQuery extraflow = new EcmUserExtraflowQuery();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		extraflow.setCurrentDateTime(pattern.format(now));
		extraflow.setFkUserId(ecmUser.getPkUserId());
		List<EcmUserExtraflow> selectByExtraflow = ecmUserExtraflowDao.selectByExtraflow(extraflow);
		if (selectByExtraflow != null && !selectByExtraflow.isEmpty()) {
			totalflow += selectByExtraflow.size()*5*1024*1024;
		}
		EcmUserVipQuery vip = new EcmUserVipQuery();
		vip.setFkUserId(ecmUser.getPkUserId());
		vip.setCurrentDateTime(pattern.format(now));
		EcmUserVip vipInfo = ecmUserVipDao.selectByVipUser(vip);
		if (selectByExtraflow != null && vipInfo.getVipId() != null) {
			totalflow += 10*1024*1024;
		}
		user.setUserFlow(ecmUserFlow.getSurplusFlow());
		user.setTotalFlow(totalflow);

		return ResponseDTO.ok(SUCCESS,user);
	}
	
	/**
	 * @param: [EcmUserHistoryFlowVO] 用户上传的视频的历史信息
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: SJ
     * @Date: 2020/10/13
     * 	描述 :   流量的查询和剩余流量的计算 并判断用户是否可以上传当前视频
	 */
	@Override
	public ResponseDTO inspectFlow(EcmUserFlowQuery ecmUserFlowQuery) {
		EcmUserFlowVO userFlow = ecmUserFlowDao.selectByPkUserId(ecmUserFlowQuery.getPkUserId());
		if (Integer.valueOf(ecmUserFlowQuery.getVideoFlow()) > flowMax ){
			return ResponseDTO.fail("视频大于500M，请减小视频大小");
		}
		if (userFlow.getSurplusFlow() >= Integer.valueOf(ecmUserFlowQuery.getVideoFlow())){
			return ResponseDTO.ok("流量足够，可以上传");
		}
		return ResponseDTO.fail("流量不足，请充值");
	}
	
	/**
	 * @param: [EcmUserHistoryFlowVO] 用户上传的视频的历史信息
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: SJ
     * @Date: 2020/10/13
     * 	描述 :   流量的查询和剩余流量的计算
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
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
