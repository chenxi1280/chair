package com.mpic.evolution.chair.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.mpic.evolution.chair.common.constant.CommonField;
import com.mpic.evolution.chair.common.constant.JudgeConstant;
import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.dao.*;
import com.mpic.evolution.chair.pojo.entity.*;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkBroadcastHotVO;
import com.mpic.evolution.chair.pojo.vo.FreeAdVo;
import com.mpic.evolution.chair.service.EcmDownLinkFlowService;
import com.mpic.evolution.chair.service.VideoHandleConsumerService;
import com.mpic.evolution.chair.util.RedisUtil;
import com.qcloud.vod.common.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.service.EcmArtworkManagerService;
import com.mpic.evolution.chair.util.AIVerifyUtil;
import com.mpic.evolution.chair.util.JWTUtil;

import static com.mpic.evolution.chair.common.constant.CommonField.*;

/**
* @author 作者 SJ:
* @date 创建时间：2020-9-4 10:16:31
*/

@Service
public class EcmArtworkManagerServiceImpl implements EcmArtworkManagerService{

	@Resource
    EcmArtworkDao ecmArtworkDao;

	@Resource
	EcmArtworkNodesDao ecmArtworkNodesDao;

	@Resource
	EcmArtworkBroadcastHotDao ecmArtworkBroadcastHotDao;

	@Resource
	VideoHandleConsumerService videoHandleConsumerService;

	@Resource
	EcmDownlinkFlowDao ecmDownlinkFlowDao;

	@Resource
	EcmDownlinkFlowHistoryDao ecmDownlinkFlowHistoryDao;

	@Resource
	EcmDownLinkFlowService ecmDownLinkFlowService;

	@Resource
	RedisUtil redisUtil;

	@Resource
	EcmArtworkFreeAdDao ecmArtworkFreeAdDao;

	@Resource
	EcmSubappUpdateHistoryDao ecmSubappUpdateHistoryDao;

	@Resource
	EcmArtworkCompressionFreeDao ecmArtworkCompressionFreeDao;

	@Override
	public ResponseDTO modifyArtWorkStatus(EcmArtworkVo ecmArtworkVo) {
		JSONObject message = this.getMessage();
//		delete 删除 publish发布 cancel撤销审核 verify 提交审核
		try {
			if (StringUtil.isEmpty(ecmArtworkVo.getCode())) {
				return ResponseDTO.fail(JudgeConstant.FAIL);
			}
			JSONObject condition = this.getCondition();
			EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtworkVo.getPkArtworkId());
			ecmArtworkVo.setLastModifyDate(new Date());
			//  作品通过审核发布
			Boolean sendFail = false ;
			if ( CommonField.PUBLISH.equals(ecmArtworkVo.getCode()) ){
				if (ecmArtwork.getArtworkStatus() == INT_TWO  ||  ecmArtwork.getArtworkStatus() == INT_THREE ){
					EcmArtworkBroadcastHotVO ecmArtworkBroadcastHotVO = ecmArtworkBroadcastHotDao.selectByArtworkId(ecmArtworkVo.getPkArtworkId());
					if (ecmArtworkBroadcastHotVO == null){
						ecmArtworkBroadcastHotVO  = new EcmArtworkBroadcastHotVO();
						ecmArtworkBroadcastHotVO.setWaitCount(0);
						ecmArtworkBroadcastHotVO.setBroadcastCount(0);
						ecmArtworkBroadcastHotVO.setFkArkworkId(ecmArtworkVo.getPkArtworkId());
						ecmArtworkBroadcastHotDao.insertSelective(ecmArtworkBroadcastHotVO);
					}
					ecmArtworkVo.setArtworkStatus((short)4);
					// 作者发布作品时 预热视频
					ecmDownLinkFlowService.pushUrlCache(ecmArtworkVo.getPkArtworkId(),ecmArtworkVo.getFkUserid());
				}else {
					return ResponseDTO.fail(JudgeConstant.FAIL);
				}
			}

			//  作品请求审核
			if ( CommonField.VERIFY.equals(ecmArtworkVo.getCode())){
				// 全景视频 不走腾讯审核
				if (ecmArtwork.getPlayMode() != 2 ) {
					videoHandleConsumerService.handleArtwork(ecmArtworkVo.getPkArtworkId());
				}
				ecmArtworkVo.setArtworkStatus((short)1);
			}
			if ( CommonField.CANCEL.equals(ecmArtworkVo.getCode() )){
				ecmArtworkVo.setArtworkStatus((short)0);
			}
			if ( CommonField.DELETE.equals(ecmArtworkVo.getCode() )){
				ecmArtworkVo.setArtworkStatus((short)5);
			}

			ecmArtworkDao.updateByPrimaryKeySelective(ecmArtworkVo);
			return ResponseDTO.ok(message.getString(ecmArtworkVo.getCode())+"成功");
		} catch (Exception e) {
			return ResponseDTO.fail(message.getString(ecmArtworkVo.getCode())+"失败");
		}

	}

	@Override
	public ResponseDTO addArtWorks(EcmArtworkVo ecmArtworkVo) {
		try {
			EcmArtwork ecmArtwork = new EcmArtwork();
			ecmArtwork.setPkArtworkId(ecmArtworkVo.getPkArtworkId());
			Integer userId = this.getIdByToken(ecmArtworkVo.getToken());
			if(userId == null){
				return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
			}
			ecmArtwork.setFkUserid(userId);
			String artworkName = ecmArtworkVo.getArtworkName();
			if (StringUtils.isEmpty(artworkName)) {
				return ResponseDTO.fail("作品名称不能为空");
			}
			String result = AIVerifyUtil.convertContent(artworkName);
			if (!StringUtils.isEmpty(result)) {
				return ResponseDTO.fail(ErrorEnum.ERR_510.getText(),result,null,ErrorEnum.ERR_510.getValue());
			}

			// 用户在设置免广告播放的时候 要查询用户是否有足够的下行流量 不进行短信通知 新用户查询不到下行流量信息的时候我们直接返回错误状态
			// 如果没有设置免广告则不需要查询下行流量
			if(ecmArtworkVo.getPlayType() == 1){
				boolean b = this.checkdownLinkFlowIsEmpty(userId);
				if(!b){
					return this.flowDetectionFailed(userId);
				}
			}

			ecmArtwork.setArtworkName(artworkName);
			ecmArtwork.setPlayMode(ecmArtworkVo.getPlayMode());
			ecmArtwork.setArtworkStatus((short)0);
			ecmArtwork.setArtworkDescribe(ecmArtworkVo.getArtworkDescribe());
			ecmArtwork.setFourLetterTips(ecmArtworkVo.getFourLetterTips());
			ecmArtwork.setLastCreateDate(new Date());
			ecmArtwork.setLastModifyDate(new Date());
			ecmArtwork.setLogoPath(ecmArtworkVo.getLogoPath());
			ecmArtwork.setPlayType(ecmArtworkVo.getPlayType());
			ecmArtworkDao.insertSelective(ecmArtwork);
			EcmArtworkNodes ecmArtworkNodes = new EcmArtworkNodes();
			ecmArtworkNodes.setFkArtworkId(ecmArtwork.getPkArtworkId());
			ecmArtworkNodes.setParentId(0);
			ecmArtworkNodes.setIsleaf("N");
			ecmArtworkNodes.setRevolutionId("x");
			ecmArtworkNodes.setALevel(0);
			ecmArtworkNodes.setItemsBakText("https://sike-1259692143.cos.ap-chongqing.myqcloud.com/img/1604281276527nodeImgUrl.png");
			ecmArtworkNodes.setVideoText("开场");
			ecmArtworkNodesDao.insertSelective(ecmArtworkNodes);

			//在有下行流量的情况下 进行免压缩的逻辑操作
			if(ecmArtworkVo.getPlayType() == 1) {
				//保存免广告记录
				EcmArtworkFreeAd ecmArtworkFreeAd = new EcmArtworkFreeAd();
				ecmArtworkFreeAd.setFkArtworkId(ecmArtwork.getPkArtworkId());
				ecmArtworkFreeAd.setCreateTime(new Date());
				ecmArtworkFreeAdDao.insertSelective(ecmArtworkFreeAd);
				if (ecmArtworkVo.getVideoType() == 1) {
					this.artworkCompressionFree(userId, ecmArtwork.getPkArtworkId());
				}
			}
			return ResponseDTO.ok("新建成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.fail("新建失败");
		}
	}

	@Override
	public ResponseDTO modifyArtWork(EcmArtworkVo ecmArtworkVo) {
		try {
			EcmArtwork ecmArtwork = new EcmArtwork();
			ecmArtwork.setPkArtworkId(ecmArtworkVo.getPkArtworkId());
			Integer userId = this.getIdByToken(ecmArtworkVo.getToken());
			if(userId == null){
				return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
			}
			ecmArtwork.setFkUserid(userId);
			ecmArtwork.setArtworkStatus(ecmArtworkVo.getArtworkStatus());
			String artworkName = ecmArtworkVo.getArtworkName();
			if (StringUtils.isEmpty(artworkName)) {
				return ResponseDTO.fail("作品名称不能为空");
			}
			String result = AIVerifyUtil.convertContent(artworkName);
			if (!StringUtils.isEmpty(result)) {
				return ResponseDTO.fail(ErrorEnum.ERR_510.getText(),result,null,ErrorEnum.ERR_510.getValue());
			}
			// 用户在设置免广告播放的时候 要查询用户是否有足够的下行流量 不进行短信通知 新用户查询不到下行流量信息的时候我们直接返回错误状态
			// 如果没有设置免广告则不需要查询下行流量
			if(ecmArtworkVo.getPlayType() == 1){
				boolean b = this.checkdownLinkFlowIsEmpty(userId);
				if(!b){
					return this.flowDetectionFailed(userId);
				}
				EcmArtworkFreeAd ecmArtworkFreeAd = new EcmArtworkFreeAd();
				ecmArtworkFreeAd.setFkArtworkId(ecmArtworkVo.getPkArtworkId());
				EcmArtworkFreeAd ecmArtworkFreeAd1 = ecmArtworkFreeAdDao.selectByRecord(ecmArtworkFreeAd);
				if(ecmArtworkFreeAd1 == null) {
					ecmArtworkFreeAd.setCreateTime(new Date());
					ecmArtworkFreeAdDao.insertSelective(ecmArtworkFreeAd);
				}
				if(ecmArtworkVo.getVideoType() == 1){
					this.artworkCompressionFree(userId,ecmArtworkVo.getPkArtworkId());
				}
			}else{
				EcmArtworkFreeAd ecmArtworkFreeAd = new EcmArtworkFreeAd();
				ecmArtworkFreeAd.setFkArtworkId(ecmArtworkVo.getPkArtworkId());
				ecmArtworkFreeAd = ecmArtworkFreeAdDao.selectByRecord(ecmArtworkFreeAd);
				if(ecmArtworkFreeAd != null) {
					ecmArtworkFreeAdDao.deleteByPrimaryKey(ecmArtworkFreeAd.getPkEcmArtworkFreeAdId());
				}
			}
			ecmArtwork.setPlayMode(ecmArtworkVo.getPlayMode());
			ecmArtwork.setArtworkName(artworkName);
			ecmArtwork.setLogoPathStatus((short)0);
			ecmArtwork.setLogoPath(ecmArtworkVo.getLogoPath());
			ecmArtwork.setLastModifyDate(new Date());
			ecmArtwork.setPlayType(ecmArtworkVo.getPlayType());
			ecmArtwork.setFourLetterTips(ecmArtworkVo.getFourLetterTips());
			ecmArtwork.setArtworkDescribe(ecmArtworkVo.getArtworkDescribe());
//			ecmArtwork.setLogoPath(ecmArtworkVo.getLogoPath());
 			ecmArtworkDao.updateByPrimaryKeySelective(ecmArtwork);
			return ResponseDTO.ok("作品名称修改成功",ecmArtworkVo.getArtworkDescribe());
		} catch (Exception e) {
			return ResponseDTO.fail("修改失败");
		}
	}

	/**
	 * 未通过下行流量检测
	 * @param userId
	 * @return
	 */
	private ResponseDTO flowDetectionFailed(Integer userId){
		EcmDownlinkFlow ecmDownlinkFlow = new EcmDownlinkFlow();
		ecmDownlinkFlow.setFkUserId(userId);
		ecmDownlinkFlow = ecmDownlinkFlowDao.selectByRecord(ecmDownlinkFlow);
		//ecmDownlinkFlow 用户没有下行流量记录的话标识未开通云点播子应用
		if(ecmDownlinkFlow == null){
			return ResponseDTO.fail(ErrorEnum.ERR_10085.getText(),null,null,ErrorEnum.ERR_10085.getValue());
		}else{
			//用户流量为0 时停用云点播
			String subAppId = ecmDownlinkFlow.getSubAppId().toString();
			boolean symbol = ecmDownLinkFlowService.modifySubAppStatus("Off", Long.valueOf(subAppId));
			if(symbol){
				EcmSubappUpdateHistory ecmSubappUpdateHistory = new EcmSubappUpdateHistory();
				ecmSubappUpdateHistory.setSubAppId(ecmDownlinkFlow.getSubAppId());
				ecmSubappUpdateHistory.setStatus(1);
				ecmSubappUpdateHistory.setFkUserId(userId);
				EcmSubappUpdateHistory ecmSubappUpdateHistory1 = ecmSubappUpdateHistoryDao.selectByRecord(ecmSubappUpdateHistory);
				if(ecmSubappUpdateHistory1 == null){
					ecmSubappUpdateHistory.setStatus(0);
					ecmSubappUpdateHistory.setCreateTime(new Date());
					ecmSubappUpdateHistory.setUpdateTime(new Date());
					ecmSubappUpdateHistoryDao.insertSelective(ecmSubappUpdateHistory);
				}else{
					ecmSubappUpdateHistory.setStatus(0);
					ecmSubappUpdateHistory.setPkId(ecmSubappUpdateHistory1.getPkId());
					ecmSubappUpdateHistory.setUpdateTime(new Date());
					ecmSubappUpdateHistoryDao.updateByPrimaryKeySelective(ecmSubappUpdateHistory);
				}

			}
			return ResponseDTO.fail(ErrorEnum.ERR_10086.getText(),null,null,ErrorEnum.ERR_10086.getValue());
		}
	}

	/**
	 * 未通过下行流量检测
	 * @param userId
	 * @return
	 */
	private void artworkCompressionFree(Integer userId,Integer artworkId){
		// 如果是免压缩作品 并且经过了免广告的下行流量检测 则需要插入记录
		EcmArtworkCompressionFree ecmArtworkCompressionFree = new EcmArtworkCompressionFree();
		ecmArtworkCompressionFree.setStatus(1);
		ecmArtworkCompressionFree.setFkUserId(userId);
		ecmArtworkCompressionFree.setFkArtworkId(artworkId);
		EcmArtworkCompressionFree ecmArtworkCompressionFree1 = ecmArtworkCompressionFreeDao.selectByRecord(ecmArtworkCompressionFree);
		if(ecmArtworkCompressionFree1 == null){
			ecmArtworkCompressionFree.setCreateTime(new Date());
			ecmArtworkCompressionFree.setUpdateTime(new Date());
			ecmArtworkCompressionFreeDao.insertSelective(ecmArtworkCompressionFree);
		}else{
			ecmArtworkCompressionFree.setPkId(ecmArtworkCompressionFree1.getPkId());
			ecmArtworkCompressionFree.setUpdateTime(new Date());
			ecmArtworkCompressionFreeDao.updateByPrimaryKeySelective(ecmArtworkCompressionFree);
		}
	}


	private JSONObject getCondition() {
		JSONObject condition = new JSONObject();
		condition.put("delete", 5);
		condition.put("publish", 4);
		condition.put("cancel", 0);
		condition.put("verify", 1);
		return condition;
	}

	private JSONObject getMessage() {
		JSONObject message = new JSONObject();
		message.put("delete", "删除");
		message.put("publish", "发布");
		message.put("cancel", "撤销审核");
		message.put("verify", "提交审核");
		return message;
	}

	private Integer getIdByToken(String token) {
		if (!StringUtils.isNotBlank(token)) {
			return null;
		}
    	String userIdStr = JWTUtil.getUserId(token);
    	Integer userId = null;
    	if(StringUtils.isNotBlank(userIdStr) && NumberUtils.isParsable(userIdStr)){
    		userId = Integer.parseInt(userIdStr);
    	}
		return userId;
	}

	/**
	 * 接口
	 * 检查用户是否可以开启免广告
	 * @return
	 */
	public ResponseDTO checkFreeAd(FreeAdVo freeAdVo){
		Integer userId = freeAdVo.getUserId();
		if(userId == null){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
		if(freeAdVo.getPlayType() == 1){
			boolean b = this.checkdownLinkFlowIsEmpty(userId);
			if(!b){
				return this.flowDetectionFailed(userId);
			}
			EcmArtworkFreeAd ecmArtworkFreeAd = new EcmArtworkFreeAd();
			ecmArtworkFreeAd.setFkArtworkId(freeAdVo.getArtworkId());
			EcmArtworkFreeAd ecmArtworkFreeAd1 = ecmArtworkFreeAdDao.selectByRecord(ecmArtworkFreeAd);
			if(ecmArtworkFreeAd1 == null) {
				ecmArtworkFreeAd.setCreateTime(new Date());
				ecmArtworkFreeAdDao.insertSelective(ecmArtworkFreeAd);
			}
			if(freeAdVo.getVideoType() != null && freeAdVo.getVideoType() == 1){
				this.artworkCompressionFree(userId,freeAdVo.getArtworkId());
			}else{
				EcmArtworkCompressionFree ecmArtworkCompressionFree = new EcmArtworkCompressionFree();
				ecmArtworkCompressionFree.setStatus(1);
				ecmArtworkCompressionFree.setFkUserId(userId);
				ecmArtworkCompressionFree.setFkArtworkId(freeAdVo.getArtworkId());
				EcmArtworkCompressionFree ecmArtworkCompressionFree1 = ecmArtworkCompressionFreeDao.selectByRecord(ecmArtworkCompressionFree);
				if(ecmArtworkCompressionFree1 != null){
					ecmArtworkCompressionFreeDao.deleteByPrimaryKey(ecmArtworkCompressionFree1.getPkId());
				}
			}
		}else{
			EcmArtworkFreeAd ecmArtworkFreeAd = new EcmArtworkFreeAd();
			ecmArtworkFreeAd.setFkArtworkId(freeAdVo.getArtworkId());
			ecmArtworkFreeAd = ecmArtworkFreeAdDao.selectByRecord(ecmArtworkFreeAd);
			if(ecmArtworkFreeAd != null) {
				ecmArtworkFreeAdDao.deleteByPrimaryKey(ecmArtworkFreeAd.getPkEcmArtworkFreeAdId());
			}
			EcmArtworkCompressionFree ecmArtworkCompressionFree = new EcmArtworkCompressionFree();
			ecmArtworkCompressionFree.setStatus(1);
			ecmArtworkCompressionFree.setFkUserId(userId);
			ecmArtworkCompressionFree.setFkArtworkId(freeAdVo.getArtworkId());
			EcmArtworkCompressionFree ecmArtworkCompressionFree1 = ecmArtworkCompressionFreeDao.selectByRecord(ecmArtworkCompressionFree);
			if(ecmArtworkCompressionFree1 != null){
				ecmArtworkCompressionFreeDao.deleteByPrimaryKey(ecmArtworkCompressionFree1.getPkId());
			}
		}
		return ResponseDTO.ok("设置成功");
	}

	private boolean checkdownLinkFlowIsEmpty(Integer userId){
		EcmDownlinkFlow ecmDownlinkFlow = new EcmDownlinkFlow();
		ecmDownlinkFlow.setFkUserId(userId);
		ecmDownlinkFlow = ecmDownlinkFlowDao.selectByRecord(ecmDownlinkFlow);
		//先查询用户是否开通了云点播下行流量
		if(ecmDownlinkFlow == null){
			return false;
		}
		//查询完之前发现流量不足直接返回提示信息 不做继续做业务
		if(ecmDownlinkFlow.getSubTotalFlow() - ecmDownlinkFlow.getSubUsedFlow() <= 0){
			//下行流量使用完 返回提示信息
			return false;
		}
		Integer subAppId = ecmDownlinkFlow.getSubAppId();
		LocalDateTime now = LocalDateTime.now();
		//获取当前时间的年月日 时
		int dayOfMonth = now.getDayOfMonth();
		int year = now.getYear();
		int monthValue = now.getMonthValue();
		String redisKey = "chair-EcmArtworkManagerServiceImpl-checkdownLinkFlowIsEmpty-" + "flow_" + userId;
		if(!redisUtil.hasKey(redisKey)){
			redisUtil.set(redisKey,"",300);

			//今天 分两次查询云点播下行流量
			LocalDateTime yStartDateTime = LocalDateTime.of(year, monthValue, dayOfMonth - 1, 0, 0, 0);
			ZonedDateTime yesterdayStartZoned = yStartDateTime.atZone(ZoneId.from(ZoneOffset.UTC));
			LocalDateTime yesterdayStartTime = yesterdayStartZoned.toLocalDateTime();
			yesterdayStartTime = yesterdayStartTime.plusHours(-8);
			String yesterdayStart = yesterdayStartTime.toString()+":00Z";

			LocalDateTime yEndDateTime = LocalDateTime.of(year, monthValue, dayOfMonth - 1, 23, 59, 59);
			ZonedDateTime yesterdayEndZoned = yEndDateTime.atZone(ZoneId.from(ZoneOffset.UTC));
			LocalDateTime yesterdayEndTime = yesterdayEndZoned.toLocalDateTime();
			yesterdayEndTime = yesterdayEndTime.plusHours(-8);
			yesterdayEndTime = yesterdayEndTime.plusSeconds(1);
			String  yesterdayEnd = yesterdayEndTime.toString()+":00Z";


			ZonedDateTime todayEndZoned = now.atZone(ZoneId.from(ZoneOffset.UTC));
			LocalDateTime todayEndTime = todayEndZoned.toLocalDateTime();
			todayEndTime = todayEndTime.plusHours(-8);
			String[] todayEndSplit = todayEndTime.toString().split("\\.");
			String todayEnd = null;
			if(todayEndSplit[0].length()+1 == yesterdayEnd.length()){
				todayEnd = todayEndSplit[0]+"Z";
			}else{
				todayEnd = todayEndSplit[0]+":00Z";
			}

			long yesterdaySum = ecmDownLinkFlowService.describeCDNStatDetails(yesterdayStart, yesterdayEnd, subAppId);
			long todaySum = ecmDownLinkFlowService.describeCDNStatDetails(yesterdayEnd, todayEnd, subAppId);

			//更新历史记录表中昨天的记录 插入 或者更新
			EcmDownlinkFlowHistory ecmDownlinkFlowHistory = new EcmDownlinkFlowHistory();
			LocalDateTime todayCreateLocalDateTime = LocalDateTime.of(year,monthValue,dayOfMonth,0,0,0);
			LocalDateTime yesterdayCreateLocalDateTime = LocalDateTime.of(year,monthValue,dayOfMonth-1,0,0,0);
			ZoneId zoneId = ZoneId.systemDefault();
			ZonedDateTime todayZdt = todayCreateLocalDateTime.atZone(zoneId);
			ZonedDateTime  yesterdayZdt = yesterdayCreateLocalDateTime.atZone(zoneId);
			Date todayCreateDate = Date.from(todayZdt.toInstant());
			Date yesterdayCreateDate = Date.from(yesterdayZdt.toInstant());
			ecmDownlinkFlowHistory.setCreateTime(todayCreateDate);
			EcmDownlinkFlowHistory today = ecmDownlinkFlowHistoryDao.selectByRecord(ecmDownlinkFlowHistory);
			ecmDownlinkFlowHistory.setCreateTime(yesterdayCreateDate);
			EcmDownlinkFlowHistory yesterday = ecmDownlinkFlowHistoryDao.selectByRecord(ecmDownlinkFlowHistory);
			if(today == null){
				//插入一条今天的记录
				ecmDownlinkFlowHistory.setCreateTime(todayCreateDate);
				ecmDownlinkFlowHistory.setStartTime(todayCreateDate);
				ecmDownlinkFlowHistory.setEndTime(todayCreateDate);
				ecmDownlinkFlowHistory.setFkUserId(userId);
				ecmDownlinkFlowHistory.setSubAppId(subAppId);
				ecmDownlinkFlowHistory.setSubFlowStatus(0);
				ecmDownlinkFlowHistory.setSubUsedFlow(todaySum/1024);//byte 转 KB
				ecmDownlinkFlowHistoryDao.insertSelective(ecmDownlinkFlowHistory);
			}else{
				//更新今天的记录
				today.setSubUsedFlow(todaySum/1024);//byte 转 KB
				ecmDownlinkFlowHistory.setCreateTime(today.getCreateTime());
				//ecmDownlinkFlowHistory带的参数 是where条件
				ecmDownlinkFlowHistoryDao.updateBySelective(today,ecmDownlinkFlowHistory);
			}

			if(yesterday == null){
				//插入一条昨天的记录
				ecmDownlinkFlowHistory.setCreateTime(yesterdayCreateDate);
				ecmDownlinkFlowHistory.setStartTime(yesterdayCreateDate);
				ecmDownlinkFlowHistory.setEndTime(yesterdayCreateDate);
				ecmDownlinkFlowHistory.setFkUserId(userId);
				ecmDownlinkFlowHistory.setSubAppId(subAppId);
				ecmDownlinkFlowHistory.setSubFlowStatus(0);
				ecmDownlinkFlowHistory.setSubUsedFlow(yesterdaySum/1024);//byte 转 KB
				ecmDownlinkFlowHistoryDao.insertSelective(ecmDownlinkFlowHistory);
			}else{
				//更新昨天的记录
				yesterday.setSubUsedFlow(yesterdaySum/1024);//byte 转 KB
				ecmDownlinkFlowHistory.setCreateTime(yesterday.getCreateTime());
				ecmDownlinkFlowHistoryDao.updateBySelective(yesterday,ecmDownlinkFlowHistory);
			}

			EcmDownlinkFlowHistory downlinkFlowHistory = new EcmDownlinkFlowHistory();
			downlinkFlowHistory.setFkUserId(userId);
			List<EcmDownlinkFlowHistory> ecmDownlinkFlowHistories = ecmDownlinkFlowHistoryDao.selectBySelective(downlinkFlowHistory);
			long sum = 0;
			for (int i = 0; i < ecmDownlinkFlowHistories.size(); i++) {
				sum += ecmDownlinkFlowHistories.get(i).getSubUsedFlow();
			}
			//更新下行流量表记录
			ecmDownlinkFlow.setSubUsedFlow(sum);
			ecmDownlinkFlow.setUpdateTime(new Date());
			ecmDownlinkFlowDao.updateByPrimaryKeySelective(ecmDownlinkFlow);//单位Byte

		}
		long todaySumByKb = ecmDownlinkFlow.getSubUsedFlow();//单位KB
		//查询完之后发现流量不足直接返回提示信息
		if((ecmDownlinkFlow.getSubTotalFlow() - todaySumByKb) < 0){
			//下行流量使用完 返回提示信息
			return false;
		}
		return true;
	}

}
