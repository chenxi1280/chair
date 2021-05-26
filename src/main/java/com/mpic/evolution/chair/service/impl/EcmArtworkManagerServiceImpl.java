package com.mpic.evolution.chair.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.annotation.Resource;

import com.mpic.evolution.chair.common.constant.CommonField;
import com.mpic.evolution.chair.common.constant.JudgeConstant;
import com.mpic.evolution.chair.dao.*;
import com.mpic.evolution.chair.pojo.entity.*;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkBroadcastHotVO;
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
			ecmArtwork.setFkUserid(userId);
			String artworkName = ecmArtworkVo.getArtworkName();
			if (StringUtils.isEmpty(artworkName)) {
				return ResponseDTO.fail("作品名称不能为空");
			}
			String result = AIVerifyUtil.convertContent(artworkName);
			if (!StringUtils.isEmpty(result)) {
				return ResponseDTO.fail("作品名称违规含有违禁词",result,null,510);
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
			// 用户在设置免广告播放的时候 要查询用户是否有足够的下行流量 不进行短信通知 新用户查询不到下行流量信息的时候我们直接返回错误状态
			// 如果没有设置免广告则不需要查询下行流量
			if(ecmArtworkVo.getPlayType() == 1){
				boolean b = this.checkdownLinkFlowIsEmpty(userId);
				if(!b){
					return ResponseDTO.fail("设置免流量功能失败，可能原因：1.尚未购买下行流量，2.下行流量已用完，请联系我们。");
				}
				EcmArtworkFreeAd ecmArtworkFreeAd = new EcmArtworkFreeAd();
				ecmArtworkFreeAd.setFkArtworkId(ecmArtwork.getPkArtworkId());
				ecmArtworkFreeAd.setCreateTime(new Date());
				ecmArtworkFreeAdDao.insertSelective(ecmArtworkFreeAd);
				videoHandleConsumerService.copyVideo(ecmArtwork.getPkArtworkId());
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
			ecmArtwork.setFkUserid(userId);
			ecmArtwork.setArtworkStatus(ecmArtworkVo.getArtworkStatus());
			String artworkName = ecmArtworkVo.getArtworkName();
			if (StringUtils.isEmpty(artworkName)) {
				return ResponseDTO.fail("作品名称不能为空");
			}
			String result = AIVerifyUtil.convertContent(artworkName);
			if (!StringUtils.isEmpty(result)) {
				return ResponseDTO.fail("作品名称违规含有违禁词",result,null,510);
			}
			// 用户在设置免广告播放的时候 要查询用户是否有足够的下行流量 不进行短信通知 新用户查询不到下行流量信息的时候我们直接返回错误状态
			// 如果没有设置免广告则不需要查询下行流量
			if(ecmArtworkVo.getPlayType() == 1){
				boolean b = this.checkdownLinkFlowIsEmpty(userId);
				if(!b){
					return ResponseDTO.fail("设置免流量功能失败，可能原因：1.尚未购买下行流量，2.下行流量已用完，请联系我们。");
				}
				EcmArtworkFreeAd ecmArtworkFreeAd = new EcmArtworkFreeAd();
				ecmArtworkFreeAd.setFkArtworkId(ecmArtworkVo.getPkArtworkId());
				EcmArtworkFreeAd ecmArtworkFreeAd1 = ecmArtworkFreeAdDao.selectByRecord(ecmArtworkFreeAd);
				if(ecmArtworkFreeAd1 == null) {
					ecmArtworkFreeAd.setCreateTime(new Date());
					ecmArtworkFreeAdDao.insertSelective(ecmArtworkFreeAd);
				}
				videoHandleConsumerService.copyVideo(ecmArtwork.getPkArtworkId());
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
		String redisKey = "flow_" + userId;
		if(!redisUtil.hasKey(redisKey)){
			//今天 分两次查询云点播下行流量
			LocalDateTime yStartDateTime = LocalDateTime.of(year, monthValue, dayOfMonth - 1, 0, 0, 0);
			ZonedDateTime yesterdayStartZoned = yStartDateTime.atZone(ZoneId.from(ZoneOffset.UTC));
			ZonedDateTime yesterdayStartconverted = yesterdayStartZoned.withZoneSameInstant(ZoneOffset.ofHours(-8));
			LocalDateTime yesterdayStartTimeUTC = yesterdayStartconverted.toLocalDateTime();
			String[] yesterdayStartSplit = yesterdayStartTimeUTC.toString().split("\\.");
			String yesterdayStartTime = yesterdayStartSplit[0]+"Z";

			LocalDateTime yEndDateTime = LocalDateTime.of(year, monthValue, dayOfMonth - 1, 23, 59, 59);
			ZonedDateTime yesterdayEndZoned = yEndDateTime.atZone(ZoneId.from(ZoneOffset.UTC));
			ZonedDateTime yesterdayEndconverted = yesterdayEndZoned.withZoneSameInstant(ZoneOffset.ofHours(-8));
			LocalDateTime yesterdayEndUTC = yesterdayEndconverted.toLocalDateTime();
			String[] yesterdayEndSplit = yesterdayEndUTC.toString().split("\\.");
			String yesterdayEndTime = yesterdayEndSplit[0]+"Z";

			ZonedDateTime todayEndZoned = now.atZone(ZoneId.from(ZoneOffset.UTC));
			ZonedDateTime todayEndconverted = todayEndZoned.withZoneSameInstant(ZoneOffset.ofHours(-8));
			LocalDateTime todayEndUTC = todayEndconverted.toLocalDateTime();
			String[] todayEndSplit = todayEndUTC.toString().split("\\.");
			String todayEndTime = todayEndSplit[0]+"Z";

			long yesterdaySum = ecmDownLinkFlowService.describeCDNStatDetails(yesterdayStartTime, yesterdayEndTime, subAppId);
			long todaySum = ecmDownLinkFlowService.describeCDNStatDetails(yesterdayEndTime, todayEndTime, subAppId);


			redisUtil.set(redisKey,"",5);

			EcmDownlinkFlowHistory ecmDownlinkFlowHistory = new EcmDownlinkFlowHistory();
			LocalDateTime createLocalDateTime = LocalDateTime.of(year,monthValue,dayOfMonth-1,0,0,0);
			ZoneId zoneId = ZoneId.systemDefault();
			ZonedDateTime zdt = createLocalDateTime.atZone(zoneId);
			Date createDate = Date.from(zdt.toInstant());
			ecmDownlinkFlowHistory.setCreateTime(createDate);
			EcmDownlinkFlowHistory history = ecmDownlinkFlowHistoryDao.selectByRecord(ecmDownlinkFlowHistory);
			if(history == null){
				//插入一条昨天的记录
				ecmDownlinkFlowHistory.setStartTime(createDate);
				ecmDownlinkFlowHistory.setEndTime(createDate);
				ecmDownlinkFlowHistory.setFkUserId(userId);
				ecmDownlinkFlowHistory.setSubAppId(subAppId);
				ecmDownlinkFlowHistory.setSubFlowStatus(0);
				ecmDownlinkFlowHistory.setSubUsedFlow(yesterdaySum/1024);//Byte 转 KB
				ecmDownlinkFlowHistoryDao.insertSelective(ecmDownlinkFlowHistory);
			}else{
				//更新昨天的记录
				history.setSubUsedFlow(yesterdaySum/1024);//Byte 转 KB
				ecmDownlinkFlowHistoryDao.updateBySelective(history,ecmDownlinkFlowHistory);
			}
			//更新下行流量表记录
			ecmDownlinkFlow.setSubUsedFlow(todaySum/1024);//Byte 转 KB
			ecmDownlinkFlow.setUpdateTime(new Date());
			ecmDownlinkFlowDao.updateByPrimaryKeySelective(ecmDownlinkFlow);

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
