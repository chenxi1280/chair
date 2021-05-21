package com.mpic.evolution.chair.service.impl;

import static com.mpic.evolution.chair.common.constant.CommonField.*;
import static com.mpic.evolution.chair.common.constant.JudgeConstant.FLOW_MAX;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.dao.*;
import com.mpic.evolution.chair.pojo.entity.*;
import com.mpic.evolution.chair.pojo.vo.EcmUserHistoryFlowVO;
import com.mpic.evolution.chair.service.vip.BeanConfig;
import com.mpic.evolution.chair.service.vip.PaymentVipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.mpic.evolution.chair.common.constant.SecretKeyConstants;
import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmUserFlowQuery;
import com.mpic.evolution.chair.pojo.vo.EcmUserFlowVO;
import com.mpic.evolution.chair.pojo.query.EcmUserHistoryFlowQuery;
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
	EcmVipUserInfoDao ecmVipUserInfoDao;
	@Resource
	EcmVipRoleDao ecmVipRoleDao;
	@Resource
    EcmUserExtraflowDao ecmUserExtraflowDao;
	@Resource
	BeanConfig beanConfig;
	@Resource
	EcmDownlinkFlowHistoryDao ecmDownlinkFlowHistoryDao;

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
		if (flag<0) {
            return false;
        }
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
	//会员时长展示用户会员总时长 流量按月展示
	@Override
	public EcmUserVo webGetUserInfo(EcmUser ecmUser) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		EcmUserVo user = ecmUserDao.selectByPkUserId(ecmUser.getPkUserId());
		if (user == null ){
			return user;
		}
		//会员流量上限
		int vipflow = 0;
		//如何计算流量的标志 0会员月内 1会员信息重置
		int symbol = 3;
		//没有查询到有效地vip（即未过期的vip） 那就判断是否是新用户
		//如果发现是新用户赠送一个月普通会员 并获取其会员信息
		EcmUserFlowVO ecmUserFlow = ecmUserFlowDao.selectByPkUserId(ecmUser.getPkUserId());
		if (ecmUserFlow == null || ecmUserFlow.getUserFlowId() == null){
			PaymentVipService updateVipDate = beanConfig.createQueryService("UpdateSuperVipDate");
			updateVipDate.operationRelateToPayment(6,ecmUser.getPkUserId(),"新用户赠送半年超级会员");
			EcmVipRole commonRole = ecmVipRoleDao.selectByPrimaryKey(2);
			int newVipUserFlow =  commonRole.getFlowLimit()*1000*1000;
			EcmUserFlowVO userFlow= new EcmUserFlowVO();
			userFlow.setUserId(user.getPkUserId());
			userFlow.setUpdateTime(new Date());
			//新用户默认送10g永久流量
			userFlow.setTotalFlow(newVipUserFlow);
			userFlow.setCheckFlow(0);
			userFlow.setUsedFlow(0);
			userFlow.setPermanentFlow(0);
			ecmUserFlowDao.insert(userFlow);
			//更新操作对象
			ecmUserFlow = userFlow;
		}
		EcmVipUserInfo vip = new EcmVipUserInfo();
		vip.setFkUserid(ecmUser.getPkUserId());
		// 超级会员 查出的可能是多个结果
		List<EcmVipUserInfo> ecmVipUserInfos = ecmVipUserInfoDao.selectByUserInfo(vip);
		// 获取未过期得会员信息 vip可能为空说明会员已过期 并且根据vip等级进行了排序 高等级优先
		vip = this.getEffectiveVipInfo(ecmVipUserInfos);
		if (vip != null && vip.getPkId() != null) {
			//该用户具有有效地会员信息
			Date startDate = vip.getVipStartTime();
			Date endDate = vip.getVipEndTime();
			symbol = this.isResetVipFlow(startDate, endDate);
			//根据角色id 获取角色相关信息: 会员赠送流量上限
			EcmVipRole ecmVipRole = ecmVipRoleDao.selectByPrimaryKey(vip.getFkVipRoleId());
			//此处只有是有效会员才会给其加上会员流量 不是会员或者会员过期不关心 vipflow为默认0
			vipflow += ecmVipRole.getFlowLimit()*1000*1000;
		}
		//无效信息置空
		user.setPassword(null);
		user.setCardCode(null);
		user.setRoles(null);
		user.setUserLogoStatus(null);
		try {
			user.setMobile(EncryptUtil.aesDecrypt(user.getMobile(), SecretKeyConstants.SECRET_KEY));
		} catch (Exception e) {
			user.setMobile(null);
			e.printStackTrace();
		}
		//有会员信息，未到当月截止期，去掉已使用流量
		if (symbol == INT_ZORE) {
			//会员的日期 超级会员日期 当月会员总 会员已使用 会员剩余流量 永久流量总 永久流量已使用 永久流量剩余
			HashMap<String, Date> vipZone = this.getVipZone(vip.getVipStartTime());
			java.sql.Date param1 = new java.sql.Date(vipZone.get("startDate").getTime());
			java.sql.Date param2 = new java.sql.Date(vipZone.get("endDate").getTime());
			//会员未到当月截止日期 并且是超级会员
			if(vip.getFkVipRoleId() == 2){
				//如果是超级会员一定有普通会员得的信息 只展示超级会员的相关信息
				for (int i = 0; i < ecmVipUserInfos.size(); i++){
					if(ecmVipUserInfos.get(i).getFkVipRoleId() == 1){
						//循环遍历找出普通会员的信息
						//common vip数据
						String endTime = format.format(ecmVipUserInfos.get(i).getVipEndTime());
						user.setVipEndDate(endTime);
						String startTime = format.format(ecmVipUserInfos.get(i).getVipStartTime());
						user.setVipStartDate(startTime);
					}
				}
				//超级vip的数据 当前对象是superVip
				String endTime = format.format(vip.getVipEndTime());
				user.setSuperVipEndDate(endTime);
				String startTime = format.format(vip.getVipStartTime());
				user.setSuperVipStartDate(startTime);
				user = this.makeUserFlowData(vip, user, ecmUserFlow, vipflow, format.format(param1), format.format(param2));
			}
			//会员未到当月截止日期 并且是普通会员
			if(vip.getFkVipRoleId() == 1){
				String endTime = format.format(vip.getVipEndTime());
				user.setVipEndDate(endTime);
				String startTime = format.format(vip.getVipStartTime());
				user.setVipStartDate(startTime);
				//如果是普通会员超级会员信息为空
				user.setSuperVipEndDate(null);
				user.setSuperVipStartDate(null);
				user = this.makeUserFlowData(vip, user, ecmUserFlow, vipflow, format.format(param1), format.format(param2));
			}
			user.setUserFlow(ecmUserFlow.getPermanentFlow());
			user.setTotalFlow(vipflow - ecmUserFlow.getUsedFlow());
		}
		//有会员过了当前月的截止期 重置已使用流量为0
		if (symbol == INT_ONE) {
			if(vip.getFkVipRoleId() == 2){
				user.setSuperVipStartDate(format.format(vip.getVipStartTime()));
				user.setSuperVipEndDate(format.format(vip.getVipEndTime()));
				//如果是超级会员一定有普通会员得的信息 只展示超级会员的相关信息
				for (int i = 0; i < ecmVipUserInfos.size(); i++){
					if(ecmVipUserInfos.get(i).getFkVipRoleId() == 1){
						//循环遍历找出普通会员的信息
						//common vip数据
						String endTime = format.format(ecmVipUserInfos.get(i).getVipEndTime());
						user.setVipEndDate(endTime);
						String startTime = format.format(ecmVipUserInfos.get(i).getVipStartTime());
						user.setVipStartDate(startTime);
					}
				}
			}
			if(vip.getFkVipRoleId() == 1){
				user.setSuperVipStartDate(null);
				user.setSuperVipEndDate(null);
				user.setVipEndDate(format.format(vip.getVipEndTime()));
				user.setVipStartDate(format.format(vip.getVipStartTime()));
			}
			int usedFlowBySymbolEqualsOne = this.getUsedFlowBySymbolEqualsOne(vip.getVipStartTime(),ecmUser.getPkUserId(),format);
			user.setCurrentMothVipUserdFlow(usedFlowBySymbolEqualsOne);
			user.setCurrentMothVipsurplusFlow(vipflow - usedFlowBySymbolEqualsOne);
			user.setCurrentMothVipTotalFlow(vipflow);
			//查询用户永久流量充值记录
			int totalPermanentFlow = 0;
			List<EcmUserExtraflow> ecmUserExtraflows = ecmUserExtraflowDao.selectByFkUserId(user.getPkUserId());
			for (int i = 0; i < ecmUserExtraflows.size(); i++){
				EcmUserExtraflow ecmUserExtraflow = ecmUserExtraflows.get(i);
				totalPermanentFlow += ecmUserExtraflow.getExtraflowNumber();
			}
			user.setUsedPermanentFlow(totalPermanentFlow - ecmUserFlow.getPermanentFlow());
			user.setSurplusPermanentFlow(ecmUserFlow.getPermanentFlow());
			user.setTotalPermanentFlow(totalPermanentFlow);
			user.setUserFlow(ecmUserFlow.getPermanentFlow());
			user.setTotalFlow(vipflow);
		}
		//有会员信息但是过期了  将其所有数据都清零
		if (symbol == INT_THREE) {
			//查询到了会员信息 但是被有效筛查剔除 所以一定是会员到期
			user.setCurrentMothVipUserdFlow(0);
			user.setCurrentMothVipsurplusFlow(0);
			user.setCurrentMothVipTotalFlow(0);
			user.setSuperVipStartDate(null);
			user.setSuperVipEndDate(null);
			user.setVipStartDate(null);
			user.setVipEndDate(null);
			user.setUsedPermanentFlow(0);
			user.setSurplusPermanentFlow(0);
			user.setTotalPermanentFlow(0);
		}
		//与前端约定 totalFlow是用户会员的剩余流量 userFlow是用户永久流量的剩余流量
		return user;
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//TODO 1.查询用户流量的数据表变了 2.流量计算公式要加入超级会员的流量
		if (Integer.parseInt(ecmUserFlowQuery.getVideoFlow()) > FLOW_MAX ){
			return ResponseDTO.fail("视频大于500M，请减小视频大小");
		}
		//查询本次请求的时间点用户是否是vip
		int vipflow = 0;
		//如何计算流量的标志
		int symbol = 3;
		EcmVipUserInfo vipInfo = new EcmVipUserInfo();
		vipInfo.setFkUserid(ecmUserFlowQuery.getPkUserId());
		//TODO 查出的可能是多个结果
		List<EcmVipUserInfo> ecmVipUserInfos = ecmVipUserInfoDao.selectByUserInfo(vipInfo);
		vipInfo = this.getEffectiveVipInfo(ecmVipUserInfos);
		if (vipInfo != null && vipInfo.getPkId() != null) {
			Date startDate = vipInfo.getVipStartTime();
			Date endDate = vipInfo.getVipEndTime();
			symbol = this.isResetVipFlow(startDate, endDate);
			//根据角色id 获取角色相关信息: 会员赠送流量上限
			EcmVipRole ecmVipRole = ecmVipRoleDao.selectByPrimaryKey(vipInfo.getFkVipRoleId());
			//此处只有是有效会员才会给其加上会员流量 不是会员或者会员过期不关心 vipflow为默认0
			vipflow += ecmVipRole.getFlowLimit()*1000*1000;
		}
		EcmUserFlowVO userFlow = ecmUserFlowDao.selectByPkUserId(ecmUserFlowQuery.getPkUserId());
		int surplusFlow = 0;
		//有会员 但是都到期了
		if (symbol == INT_THREE) {
			return ResponseDTO.fail("流量不足，请充值");
		}
		//有会员 到本月截止日期了 会员赠送+剩余永久流量
		if (symbol == INT_ONE) {
			int usedFlowBySymbolEqualsOne = this.getUsedFlowBySymbolEqualsOne(vipInfo.getVipStartTime(),ecmUserFlowQuery.getPkUserId(),format);
			surplusFlow = vipflow + userFlow.getPermanentFlow() - usedFlowBySymbolEqualsOne;
		}
		//有会员未到本月截止日期 会员赠送+剩余永久流量-已使用会员流量
		if (symbol == INT_ZORE) {
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
		//查询用户信息
		EcmUserVo user = ecmUserDao.selectByPkUserId(ecmUserHistoryFlowVO.getUserId());
		if (user == null ){
			return ResponseDTO.fail(ErrorEnum.ERR_003.getText());
		}
		int vipflow = 0;
		//如何计算流量的标志
		int symbol = 3;
		EcmVipUserInfo vipInfo = new EcmVipUserInfo();
		vipInfo.setFkUserid(ecmUserHistoryFlowVO.getUserId());
		List<EcmVipUserInfo> ecmVipUserInfos = ecmVipUserInfoDao.selectByUserInfo(vipInfo);
		vipInfo = this.getEffectiveVipInfo(ecmVipUserInfos);
		if (vipInfo != null && vipInfo.getPkId() != null) {
			Date startDate = vipInfo.getVipStartTime();
			Date endDate = vipInfo.getVipEndTime();
			symbol = this.isResetVipFlow(startDate, endDate);
			//根据角色id 获取角色相关信息: 会员赠送流量上限
			EcmVipRole ecmVipRole = ecmVipRoleDao.selectByPrimaryKey(vipInfo.getFkVipRoleId());
			//此处只有是有效会员才会给其加上会员流量 不是会员或者会员过期不关心 vipflow为默认0
			vipflow += ecmVipRole.getFlowLimit()*1000*1000;
		}
		//判断此次上传流量该如何去扣取 先扣vip流量
		EcmUserFlowVO userFlow = ecmUserFlowDao.selectByPkUserId(ecmUserHistoryFlowVO.getUserId());
		//有会员信息，未到当月截止期，去掉已使用流量
		if(symbol == INT_ZORE){
			int totalUsedFlow = ecmUserHistoryFlowVO.getVideoFlow() + userFlow.getUsedFlow();
			if (totalUsedFlow < vipflow) {
				//如果总的已使用流量小于会员流量上限 直接写入已使用流量
				userFlow.setUsedFlow(totalUsedFlow);
			}else {
				if(totalUsedFlow == vipflow){
					//如果总的已使用流量等于会员流量上限 直接写入已使用流量
					userFlow.setUsedFlow(vipflow);
				}else{
					//如果总的已使用流量大于会员流量上限 先将会员流量扣完再扣永久流量
					int surplus = totalUsedFlow - vipflow;
					userFlow.setUsedFlow(vipflow);
					userFlow.setPermanentFlow(userFlow.getPermanentFlow() - surplus);
				}
			}
			userFlow.setTotalFlow(vipflow + userFlow.getPermanentFlow()-userFlow.getUsedFlow());
		}
		//TODO 重置已使用流量只能在会员日期内的第一次扣流量时清除 并且当月会员期内只能执行一次清除
		//有会员过了当前月的截止期 重置已使用流量为0
		if (symbol == INT_ONE) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int usedFlowBySymbolEqualsOne = this.getUsedFlowBySymbolEqualsOne(vipInfo.getVipStartTime(),ecmUserHistoryFlowVO.getUserId(),format);
			userFlow.setTotalFlow(vipflow + userFlow.getPermanentFlow() - usedFlowBySymbolEqualsOne);
			userFlow.setUsedFlow(ecmUserHistoryFlowVO.getVideoFlow() + usedFlowBySymbolEqualsOne);
		}
		//会员到期
		if (symbol == INT_THREE) {
			userFlow.setTotalFlow(0);
			userFlow.setUsedFlow(0);
			userFlow.setPermanentFlow(0);
		}
		ecmUserHistoryFlowVO.setCreatTime(new Date());
		userFlow.setUpdateTime(new Date());
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
		if (flag<0) {
            return false;
        }
		return true;
	}

	@Override
	public ResponseDTO getDownLinkFlowRecord( EcmUserHistoryFlowVO ecmUserHistoryFlowVO) {
		JSONObject data = new JSONObject();
		ArrayList<Integer> flowsSplitByDays = new ArrayList<>();
		Integer userId = ecmUserHistoryFlowVO.getUserId();
		LocalDateTime startTime = ecmUserHistoryFlowVO.getStartDate();
		LocalDateTime endTime = ecmUserHistoryFlowVO.getEndDate();
		Duration between = Duration.between(startTime, endTime);
		EcmDownlinkFlowHistory ecmDownlinkFlowHistory = new EcmDownlinkFlowHistory();
		long l = between.toDays();
		for(int i=0; i < l; i++){
			LocalDateTime localDateTime = startTime.plusDays(1);
			int year = localDateTime.getYear();
			int monthValue = localDateTime.getMonthValue();
			int dayOfMonth = localDateTime.getDayOfMonth();
			LocalDateTime targetTime= LocalDateTime.of(year, monthValue, dayOfMonth, 0, 0, 0);
			Date targetDate = VipDateUtil.formatToDate(targetTime);
			ecmDownlinkFlowHistory.setCreateTime(targetDate);
			ecmDownlinkFlowHistory.setFkUserId(userId);
			EcmDownlinkFlowHistory historyObject = ecmDownlinkFlowHistoryDao.selectByRecord(ecmDownlinkFlowHistory);
			flowsSplitByDays.add(historyObject.getSubUsedFlow()/1024);//单位KB
			startTime = targetTime;
		}
		int max = flowsSplitByDays.get(0);
		for (int i = 0; i < flowsSplitByDays.size(); i++) {
			if(flowsSplitByDays.get(i) > max){
				max = flowsSplitByDays.get(i);
			}
		}
		data.put("max",max);
		data.put("arrayData",flowsSplitByDays);
		return ResponseDTO.ok(data);
	}

	/**
	 * 判断	超级是否会员到期 会员是否过期 超级会员是否会员月到期 会员是否会员月到期
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private int isResetVipFlow(Date startDate,Date endDate) {
		LocalDateTime now = LocalDateTime.now();
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

	/**
	 * 判断	会员信息是否有效
	 * @param ecmVipUserInfos
	 * @return
	 */

	private EcmVipUserInfo getEffectiveVipInfo(List<EcmVipUserInfo> ecmVipUserInfos){
		HashMap<Integer,Integer> map = new HashMap<>();
		//如果集合只有一个元素就不用判断了
		if(ecmVipUserInfos.size() > 1){
			//筛选出过期的用户信息
			for (int i = 0; i < ecmVipUserInfos.size(); i++){
				LocalDateTime now = LocalDateTime.now();
				Date vipEndTime = ecmVipUserInfos.get(i).getVipEndTime();
				LocalDateTime endTime = VipDateUtil.formatToLocalDateTime(vipEndTime);
				if(endTime.isBefore(now)){
					ecmVipUserInfos.remove(i);
				}else{
					map.put(ecmVipUserInfos.get(i).getFkVipRoleId(),i);
				}
			}
			//按照高级角色到低级角色得筛选
			if(map.containsKey(2)){
				return ecmVipUserInfos.get(map.get(2));
			}else if(map.containsKey(1)){
				return ecmVipUserInfos.get(map.get(1));
			}else{
				return null;
			}
		}else if(ecmVipUserInfos.size() == 1){
			LocalDateTime now = LocalDateTime.now();
			Date vipEndTime = ecmVipUserInfos.get(0).getVipEndTime();
			LocalDateTime endTime = VipDateUtil.formatToLocalDateTime(vipEndTime);
			if(endTime.isBefore(now)){
				return null;
			}else{
				return ecmVipUserInfos.get(0);
			}
		}else{
			return null;
		}
	}

	/**
	 *
	 * @param startDate
	 * 当symbol为0时调用 获取当月的会员日期 startDate endDate
	 * @return
	 */
	private HashMap<String,Date> getVipZone(Date startDate){
		HashMap<String,Date> map = new HashMap<>();
		//date 转 LocalDateTime方法
		LocalDateTime now = LocalDateTime.now();
		Instant instant = startDate.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime sDate = instant.atZone(zoneId).toLocalDateTime();
		int startMonth = sDate.getMonthValue();
		int currentMonth = now.getMonthValue();
		int difference = currentMonth - startMonth;
		//若 different为0说明充值会员的时间和当前时间是同一个月

		if (difference == 0) {
			map.put("startDate",startDate);
			map.put("endDate",VipDateUtil.formatToDate(sDate.plusMonths(1)));
			return map;
		}
		//若 different不为0说明充值会员的时间和当前时间不是同一个月 所以累加相差得月份找到当前月得到期时间
		map.put("startDate",VipDateUtil.formatToDate(sDate.plusMonths(difference-1)));
		map.put("endDate",VipDateUtil.formatToDate(sDate.plusMonths(difference)));
		return map;
	}
	/**
	 *
	 * @param startDate
	 * 当symbol为1时调用 获取下个月的会员日期 startDate endDate
	 * @return
	 */
	private HashMap<String,Date> getNextMonthVipZone(Date startDate){
		HashMap<String,Date> map = new HashMap<>();
		//date 转 LocalDateTime方法
		LocalDateTime now = LocalDateTime.now();
		Instant instant = startDate.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime sDate = instant.atZone(zoneId).toLocalDateTime();
		int startMonth = sDate.getMonthValue();
		int currentMonth = now.getMonthValue();
		int difference = currentMonth - startMonth;
		//若 different为0说明充值会员的时间和当前时间是同一个月

		if (difference == 0) {
			map.put("startDate",startDate);
			map.put("endDate",VipDateUtil.formatToDate(sDate.plusMonths(1)));
			return map;
		}
		//若 different不为0说明充值会员的时间和当前时间不是同一个月 所以累加相差得月份找到当前月得到期时间 再加一个月推到下个月的会员到期时间

		//TODO 可能推算的超出了会员的截止日期 待观察
		// 也可能不会 因为我查询的时候是查询到是否有效会员就算超出当前月，但是再当前时间他是有效会员所以即使推出的截止日期是一定存在的
		map.put("startDate",VipDateUtil.formatToDate(sDate.plusMonths(difference)));
		map.put("endDate",VipDateUtil.formatToDate(sDate.plusMonths(difference+1)));
		return map;
	}

	private int getUsedFlowBySymbolEqualsOne(Date vipStartTime,Integer pkUserId,SimpleDateFormat format) {
		HashMap<String, Date> vipzoneBySymbolEqualsOneone = this.getNextMonthVipZone(vipStartTime);
		java.sql.Date param1 = new java.sql.Date(vipzoneBySymbolEqualsOneone.get("startDate").getTime());
		java.sql.Date param2 = new java.sql.Date(vipzoneBySymbolEqualsOneone.get("endDate").getTime());
		String format1 = format.format(param1);
		String format2 = format.format(param2);
		//查出会员已使用流量
		List<EcmUserHistoryFlow> ecmUserHistoryFlows = ecmUserHistoryFlowDao.selectByVipTimeZone(format1, format2, pkUserId);
		int currentMothVipUserdFlow = 0;
		for (int i = 0; i < ecmUserHistoryFlows.size(); i++) {
			EcmUserHistoryFlow ecmUserHistoryFlow = ecmUserHistoryFlows.get(i);
			currentMothVipUserdFlow += ecmUserHistoryFlow.getVideoFlow();
		}
		return currentMothVipUserdFlow;
	}

	private HashMap<String,Date> getVipZoneBySymbolEqualsOne(Date vipStartTime) {

		HashMap<String,Date> map = new HashMap<>();
		//date 转 LocalDateTime方法
		LocalDateTime now = LocalDateTime.now();
		Instant instant = vipStartTime.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime sDate = instant.atZone(zoneId).toLocalDateTime();
		map.put("startDate",VipDateUtil.formatToDate(sDate.plusMonths(1)));
		map.put("endDate",VipDateUtil.formatToDate(sDate.plusMonths(2)));int startMonth = sDate.getMonthValue();
		int currentMonth = now.getMonthValue();
		int difference = currentMonth - startMonth;

		//TODO 可能推算的超出了会员的截止日期 待观察
		// 也可能不会 因为我查询的时候是查询到是否有效会员就算超出当前月，但是再当前时间他是有效会员所以即使推出的截止日期是一定存在的
		map.put("startDate",VipDateUtil.formatToDate(sDate.plusMonths(difference)));
		map.put("endDate",VipDateUtil.formatToDate(sDate.plusMonths(difference+1)));
		return map;
	}


	private EcmUserVo makeUserFlowData(EcmVipUserInfo vip, EcmUserVo user, EcmUserFlowVO ecmUserFlow,
									   int vipflow, String param1, String param2){
		//设置会员当月流量总数
		user.setCurrentMothVipTotalFlow(vipflow);
		//查出会员已使用流量
		List<EcmUserHistoryFlow> ecmUserHistoryFlows = ecmUserHistoryFlowDao.selectByVipTimeZone(param1, param2, user.getPkUserId());
		int currentMothVipUserdFlow = 0;
		for (int i = 0; i < ecmUserHistoryFlows.size(); i++){
			EcmUserHistoryFlow ecmUserHistoryFlow = ecmUserHistoryFlows.get(i);
			currentMothVipUserdFlow += ecmUserHistoryFlow.getVideoFlow();
		}
		//查询用户永久流量充值记录
		int totalPermanentFlow = 0;
		List<EcmUserExtraflow> ecmUserExtraflows = ecmUserExtraflowDao.selectByFkUserId(user.getPkUserId());
		for (int i = 0; i < ecmUserExtraflows.size(); i++){
			EcmUserExtraflow ecmUserExtraflow = ecmUserExtraflows.get(i);
			totalPermanentFlow += ecmUserExtraflow.getExtraflowNumber();
		}
		//用户当月已使用 - 会员上限流量 若大于0 则会员surplus为0
		if(currentMothVipUserdFlow - vipflow > 0){
			user.setCurrentMothVipsurplusFlow(0);
			user.setCurrentMothVipUserdFlow(vipflow);
		}else{
			user.setCurrentMothVipsurplusFlow(vipflow - currentMothVipUserdFlow);
			user.setCurrentMothVipUserdFlow(currentMothVipUserdFlow);
		}
		user.setTotalPermanentFlow(totalPermanentFlow);
		user.setSurplusPermanentFlow(ecmUserFlow.getPermanentFlow());
		user.setUsedPermanentFlow(totalPermanentFlow - ecmUserFlow.getPermanentFlow());
		return user;
	}

}
