package com.mpic.evolution.chair.service;

import java.util.List;

import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;
import com.mpic.evolution.chair.pojo.vo.EcmInnerMessageVo;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 10:50:27 
*/
public interface EcmInnerMessageService {

	List<EcmInnerMessage> getInnerMessage(EcmUserVo user);

	boolean batchDelete(EcmInnerMessageVo ecmInnerMessageVo);

	boolean batchModifyRead(EcmInnerMessageVo ecmInnerMessageVo);

}
