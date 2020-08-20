package com.mpic.evolution.chair.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmInnerMessageDao;
import com.mpic.evolution.chair.pojo.dto.EcmInnerMessageDto;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;
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
	public List<EcmInnerMessageDto> getInnerMessage(EcmUserVo user) {
		String userId = JWTUtil.getUserId(user.getToken());
		if (StringUtils.isBlank(userId) || !NumberUtils.isParsable(userId)) {
			return null;
		}
		EcmInnerMessage ecmInnerMessage = new EcmInnerMessage();
		ecmInnerMessage.setFkUserId(Integer.valueOf(userId));
		List<EcmInnerMessageDto> messageDtos = ecmInnerMessageDao.selectByList(ecmInnerMessage);
		return messageDtos;
	}

	@Override
	public boolean deleteInnerMessage(EcmInnerMessage ecmInnerMessage) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
