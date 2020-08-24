package com.mpic.evolution.chair.service.impl;

import java.util.List;

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

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 10:50:58 
*/
@Service
public class EcmInnerMessageServiceImpl implements EcmInnerMessageService{
	
	@Autowired
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
	
}
