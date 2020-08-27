package com.mpic.evolution.chair.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.qcloud.vod.common.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmInnerMessageDao;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;
import com.mpic.evolution.chair.pojo.vo.EcmInnerMessageVo;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmInnerMessageService;
import com.mpic.evolution.chair.util.JWTUtil;

import javax.annotation.Resource;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 10:50:58 
*/
@Service
public class EcmInnerMessageServiceImpl implements EcmInnerMessageService{
	
	@Resource
	EcmInnerMessageDao ecmInnerMessageDao;

	@Override
	public List<EcmInnerMessage> getInnerMessage(EcmUserVo user) {
		String userId = JWTUtil.getUserId(user.getToken());
		if (StringUtils.isBlank(userId) || !NumberUtils.isParsable(userId)) {
			return null;
		}

		EcmInnerMessage ecmInnerMessage = new EcmInnerMessage();
		ecmInnerMessage.setFkUserId(Integer.valueOf(userId));
		List<EcmInnerMessage> messages = ecmInnerMessageDao.selectByList(ecmInnerMessage);
		return messages;
	}

	@Override
	public boolean batchDelete(EcmInnerMessageVo ecmInnerMessageVo) {
		List<Integer> messageIds = ecmInnerMessageVo.getMessageIds();
		int size = messageIds.size();
		EcmInnerMessage ecmInnerMessage = new EcmInnerMessage();
		ecmInnerMessage.setMessageStatus((short) 2);
		int update = ecmInnerMessageDao.batchUpdate(ecmInnerMessage, messageIds);
		if (update != size) {
			return false;
		}
		return true;
	}

	@Override
	public boolean batchModifyRead(EcmInnerMessageVo ecmInnerMessageVo) {
		List<Integer> messageIds = ecmInnerMessageVo.getMessageIds();
		int size = messageIds.size();
		EcmInnerMessage ecmInnerMessage = new EcmInnerMessage();
		ecmInnerMessage.setMessageStatus((short) 1);
		int update = ecmInnerMessageDao.batchUpdate(ecmInnerMessage, messageIds);
		if (update != size) {
			return false;
		}
		return true;
	}

	@Override
	public ResponseDTO getMsg(EcmUserVo user) {
		JSONObject data = new JSONObject();
		if (StringUtil.isEmpty(user.getToken())){
			return ResponseDTO.fail("网络错误");
		}
		String userId = JWTUtil.getUserId(user.getToken());
		if (StringUtils.isBlank(userId) || !NumberUtils.isParsable(userId)) {
			return null;
		}

		EcmInnerMessage ecmInnerMessage = new EcmInnerMessage();
		ecmInnerMessage.setFkUserId(Integer.valueOf(userId));
		List<EcmInnerMessageVo> messages = ecmInnerMessageDao.selectByMsgList(ecmInnerMessage);


		if (messages == null || messages.isEmpty()) {
			return ResponseDTO.fail("获取message失败");
		}


		messages = messages.stream().filter((EcmInnerMessage m)->m.getMessageStatus()<2).collect(Collectors.toList());
		messages.sort((EcmInnerMessage m1, EcmInnerMessage m2)->m2.getSendDate().compareTo(m1.getSendDate()));
		Map<Short, Long> sum = messages.stream().collect(Collectors.groupingBy(EcmInnerMessage::getMessageStatus, Collectors.counting()));

		data.put("messages", messages);
		data.put("read", sum.get((short)1));
		data.put("unread", sum.get((short)0));
		return ResponseDTO.ok("获取成功", data);
	}

}
