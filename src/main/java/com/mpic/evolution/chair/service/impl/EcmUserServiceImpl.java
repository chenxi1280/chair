package com.mpic.evolution.chair.service.impl;

import static com.mpic.evolution.chair.common.constant.JudgeConstant.SUCCESS;
import static com.mpic.evolution.chair.common.constant.JudgeConstant.flowMax;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
import com.mpic.evolution.chair.pojo.entity.EcmUserVip;
import com.mpic.evolution.chair.pojo.query.EcmUserFlowQuery;
import com.mpic.evolution.chair.pojo.vo.EcmUserFlowVO;
import com.mpic.evolution.chair.pojo.vo.EcmUserHistoryFlowVO;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.util.EncryptUtil;
import com.mpic.evolution.chair.util.VipDateUtil;

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
		EcmUserVo user = ecmUserDao.selectByPkUserId(ecmUser.getPkUserId());
		if (user == null ){
			return ResponseDTO.fail(ErrorEnum.ERR_003.getText());
		}
		int vipflow = 0;
		//如何计算流量的标志
		int symbol = 3;
		//查询vip是否到期 是否需要重置
		EcmUserVip vip = new EcmUserVip();
		vip.setFkUserId(ecmUser.getPkUserId());
		EcmUserVip vipInfo = ecmUserVipDao.selectByVipUser(vip);
		if (vipInfo != null && vipInfo.getVipId() != null) {
			Date startDate = vipInfo.getVipStartTime();
			Date endDate = vipInfo.getVipEndTime();
			symbol = this.isResetVipFlowTime(startDate, endDate);
			vipflow += 10*1024*1024;
		}
		EcmUserFlowVO ecmUserFlow = ecmUserFlowDao.selectByPkUserId(ecmUser.getPkUserId());
		//如果ecmUserFlow 为空，插入用户的新flow信息
		if (ecmUserFlow == null || ecmUserFlow.getUserFlowId() == null){
			EcmUserFlowVO userFlow= new EcmUserFlowVO();
			userFlow.setUserId(user.getPkUserId());
			userFlow.setUpdateTime(new Date());
			userFlow.setTotalFlow(512000);
			userFlow.setCheckFlow(0);
			userFlow.setUsedFlow(0);
			userFlow.setPermanentFlow(512000);
			ecmUserFlowDao.insert(userFlow);
			ecmUserFlow = userFlow;
		}
		user.setPassword(null);
		user.setCardCode(null);
		user.setRoles(null);
		user.setUserLogoStatus(null);
		try {
			user.setMobile(EncryptUtil.aesDecrypt(user.getMobile(), SecretKeyConstants.secretKey));
		} catch (Exception e) {
			user.setMobile(null);
			e.printStackTrace();
		}
		//有会员信息，未到当月截止期，去掉已使用流量
		if (symbol == 0) {
			user.setUserFlow(vipflow + ecmUserFlow.getPermanentFlow() - ecmUserFlow.getUsedFlow());
			user.setTotalFlow(vipflow + ecmUserFlow.getPermanentFlow());
		}
		//有会员过了当前月的截止期 重置已使用流量为0
		if (symbol == 1) {
			user.setUserFlow(vipflow + ecmUserFlow.getPermanentFlow());
			user.setTotalFlow(vipflow + ecmUserFlow.getPermanentFlow());
		}
		//有会员但是会员到期了
		if (symbol == 2) {
			user.setUserFlow(0);
			user.setTotalFlow(0);
		}
		//无会员信息 不是会员就无法购买加油包 只有默认流量 此流量加在永久流量中
		if (symbol == 3) {
			user.setUserFlow(ecmUserFlow.getPermanentFlow());
			user.setTotalFlow(ecmUserFlow.getPermanentFlow());
		}
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
		if (Integer.valueOf(ecmUserFlowQuery.getVideoFlow()) > flowMax ){
			return ResponseDTO.fail("视频大于500M，请减小视频大小");
		}
		//查询本次请求的时间点用户是否是vip
		int vipflow = 0;
		//如何计算流量的标志
		int symbol = 3;
		EcmUserVip vip = new EcmUserVip();
		vip.setFkUserId(ecmUserFlowQuery.getPkUserId());
		EcmUserVip vipInfo = ecmUserVipDao.selectByVipUser(vip);
		if (vipInfo != null && vipInfo.getVipId() != null) {
			Date startDate = vipInfo.getVipStartTime();
			Date endDate = vipInfo.getVipEndTime();
			symbol = this.isResetVipFlowTime(startDate, endDate);
			vipflow += 10*1024*1024;
		}
		EcmUserFlowVO userFlow = ecmUserFlowDao.selectByPkUserId(ecmUserFlowQuery.getPkUserId());
		int surplusFlow = 0;
		//无会员信息只有默认流量
		if (symbol == 3) {
			surplusFlow = userFlow.getPermanentFlow();
		}
		//会员到期 无可使用流量
		if (symbol == 2) {
			return ResponseDTO.fail("流量不足，请充值");
		}
		//有会员 会员赠送+永久流量
		if (symbol == 1) {
			surplusFlow = vipflow + userFlow.getPermanentFlow();
		}
		//有会员未到本月截止日期 会员赠送+永久流量-已使用会员流量
		if (symbol == 0) {
			surplusFlow = vipflow + userFlow.getPermanentFlow()-userFlow.getUsedFlow();
		}
		if (surplusFlow >= Integer.valueOf(ecmUserFlowQuery.getVideoFlow())){
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
		EcmUserVo user = ecmUserDao.selectByPkUserId(ecmUserHistoryFlowVO.getUserId());
		if (user == null ){
			return ResponseDTO.fail(ErrorEnum.ERR_003.getText());
		}
		//判断此次上传流量该如何去扣取
		int usedFlow = 0;
		EcmUserFlowVO userFlow = ecmUserFlowDao.selectByPkUserId(ecmUserHistoryFlowVO.getUserId());
		if (userFlow.getPermanentFlow() < ecmUserHistoryFlowVO.getVideoFlow() ) {
			if (userFlow.getPermanentFlow() == 0) {
				//如果永久使用流量扣完那就扣vip赠送
				usedFlow = ecmUserHistoryFlowVO.getVideoFlow();
			}else {
				//如果永久流量不够扣那就先把永久流量扣完再扣vip赠送
				usedFlow = ecmUserHistoryFlowVO.getVideoFlow() - userFlow.getPermanentFlow();
				userFlow.setPermanentFlow(0);
			}
		}else {
			//永久流量够扣那就先扣永久流量
			userFlow.setPermanentFlow(userFlow.getPermanentFlow() - ecmUserHistoryFlowVO.getVideoFlow());
		}
		userFlow.setUsedFlow(usedFlow);
		ecmUserHistoryFlowVO.setCreatTime(new Date());
		userFlow.setUpdateTime(new Date());
		//查询本次请求的时间点用户是否是vip
		int vipflow = 0;
		//如何计算流量的标志
		int symbol = 3;
		EcmUserVip vip = new EcmUserVip();
		vip.setFkUserId(ecmUserHistoryFlowVO.getUserId());
		EcmUserVip vipInfo = ecmUserVipDao.selectByVipUser(vip);
		if (vipInfo != null && vipInfo.getVipId() != null) {
			Date startDate = vipInfo.getVipStartTime();
			Date endDate = vipInfo.getVipEndTime();
			symbol = this.isResetVipFlowTime(startDate, endDate);
			vipflow += 10*1024*1024;
		}
		//有会员信息，未到当月截止期，去掉已使用流量
		if (symbol == 0) {
			userFlow.setTotalFlow(vipflow + userFlow.getPermanentFlow()-userFlow.getUsedFlow());
		}
		//有会员过了当前月的截止期 重置已使用流量为0
		if (symbol == 1) {
			userFlow.setTotalFlow(vipflow + userFlow.getPermanentFlow());
		}
		//有会员但是会员到期了
		if (symbol == 2) {
			userFlow.setTotalFlow(0);
			userFlow.setPermanentFlow(0);
			userFlow.setUsedFlow(0);
		}
		//无会员信息 不是会员就无法购买加油包 只有默认流量 此流量加在永久流量中
		if (symbol == 3) {
			userFlow.setTotalFlow(userFlow.getPermanentFlow());
		}
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
	
	/**
	 * 	判断是否需要重置vipFlow
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private int isResetVipFlowTime(Date startDate,Date endDate) {
		LocalDateTime now = LocalDateTime.now();
		Instant instant = endDate.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime endDateTime = instant.atZone(zoneId).toLocalDateTime();
		//在会员截至日期之前
		if (now.isBefore(endDateTime)) {
			LocalDateTime eDate = VipDateUtil.getEndDate(startDate);
			//在当月截止日期之前
			if (now.isBefore(eDate)) {
				//不需要重置vip流量
				return 0;
			}else {
				//需要重置vip流量
				return 1;
			}
		}
		//需要把所有的流量都清零
		return 2;
	}

}
