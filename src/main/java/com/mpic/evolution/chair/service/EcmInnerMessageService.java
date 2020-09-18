package com.mpic.evolution.chair.service;

import java.util.List;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;
import com.mpic.evolution.chair.pojo.query.EcmInnerMessageQurey;
import com.mpic.evolution.chair.pojo.vo.EcmInnerMessageVo;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 10:50:27 
*/
public interface EcmInnerMessageService {

	List<EcmInnerMessage> getInnerMessage(EcmUserVo user);

	ResponseDTO batchDelete(EcmInnerMessageQurey ecmInnerMessageVo);

	ResponseDTO batchModifyRead(EcmInnerMessageQurey ecmInnerMessageQurey);

	ResponseDTO getMsg(EcmUserVo user);

	EcmInnerMessageVo getUserMsgByMsgId(Integer pkMessageId);
}
