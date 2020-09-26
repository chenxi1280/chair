package com.mpic.evolution.chair.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import com.mpic.evolution.chair.common.constant.JudgeConstant;
import com.mpic.evolution.chair.dao.EcmArtworkBroadcastHotDao;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHot;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkBroadcastHotVO;
import com.mpic.evolution.chair.service.VideoHandleConsumerService;
import com.qcloud.vod.common.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.service.EcmArtworkManagerService;
import com.mpic.evolution.chair.util.AIVerifyUtil;
import com.mpic.evolution.chair.util.JWTUtil;

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
			if ( "publish".equals(ecmArtworkVo.getCode()) ){
				if (ecmArtwork.getArtworkStatus() != 4 ){
					return ResponseDTO.fail("请先审核作品");
				}
				EcmArtworkBroadcastHotVO ecmArtworkBroadcastHotVO = ecmArtworkBroadcastHotDao.selectByArtworkId(ecmArtworkVo.getPkArtworkId());
				if (ecmArtworkBroadcastHotVO == null){
					ecmArtworkBroadcastHotVO  = new EcmArtworkBroadcastHotVO();
					ecmArtworkBroadcastHotVO.setWaitCount(0);
					ecmArtworkBroadcastHotVO.setBroadcastCount(0);
					ecmArtworkBroadcastHotVO.setFkArkworkId(ecmArtworkVo.getPkArtworkId());
					ecmArtworkBroadcastHotDao.insertSelective(ecmArtworkBroadcastHotVO);
					ecmArtworkVo.setArtworkStatus((short)4);
				}
			}
			//  作品请求审核
			if ( "verify".equals(ecmArtworkVo.getCode())){
				// 重点优化需要 线程优化
				new Thread(() -> videoHandleConsumerService.handleArtwork(ecmArtworkVo.getPkArtworkId())).start();
				ecmArtworkVo.setArtworkStatus((short)1);
			}
			if ( "cancel".equals(ecmArtworkVo.getCode() )){
				ecmArtworkVo.setArtworkStatus((short)0);
			}
			if ( "delete".equals(ecmArtworkVo.getCode() )){
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
			ecmArtwork.setArtworkName(ecmArtworkVo.getArtworkName());
			ecmArtwork.setArtworkStatus((short)0);
			ecmArtwork.setArtworkDescribe(ecmArtworkVo.getArtworkDescribe());
			ecmArtwork.setFourLetterTips(ecmArtworkVo.getFourLetterTips());
			ecmArtwork.setLastCreateDate(new Date());
			ecmArtwork.setLastModifyDate(new Date());
			ecmArtwork.setLogoPath(ecmArtworkVo.getLogoPath());
			ecmArtworkDao.insert(ecmArtwork);
			EcmArtworkNodes ecmArtworkNodes = new EcmArtworkNodes();
			ecmArtworkNodes.setFkArtworkId(ecmArtwork.getPkArtworkId());
			ecmArtworkNodes.setParentId(0);
			ecmArtworkNodes.setIsleaf("N");
			ecmArtworkNodes.setRevolutionId("x");
			ecmArtworkNodes.setALevel(0);
			ecmArtworkNodes.setItemsBakText("https://sike-1259692143.cos.ap-chongqing.myqcloud.com/baseImg/1599646010668nodeImgUrl.png");
			ecmArtworkNodes.setVideoText("开场");
			ecmArtworkNodesDao.insertSelective(ecmArtworkNodes);
			return ResponseDTO.ok("新建成功");
		} catch (Exception e) {
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
			ecmArtwork.setArtworkName(artworkName);
			ecmArtwork.setLogoPathStatus((short)0);
			ecmArtwork.setLogoPath(ecmArtworkVo.getLogoPath());
			ecmArtwork.setLastModifyDate(new Date());
			ecmArtwork.setFourLetterTips(ecmArtworkVo.getFourLetterTips());
 			ecmArtworkDao.updateByPrimaryKey(ecmArtwork);
			return ResponseDTO.ok("作品名称修改成功");
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

}
