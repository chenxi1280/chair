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

	/**
	 * @param: [ecmInnerMessageQurey]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/26
	 * 描述 : 修改消息为删除 状态接口
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	ResponseDTO batchDelete(EcmInnerMessageQurey ecmInnerMessageVo);

	/**
	 * @param: [ecmInnerMessageQurey]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/26
	 * 描述 :  修改消息为已读 状态接口
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	ResponseDTO batchModifyRead(EcmInnerMessageQurey ecmInnerMessageQurey);

	/**
	 * @param: [user]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/26
	 * 描述 : 获取 用户站内信接口
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	ResponseDTO getMsg(EcmUserVo user);

	/**
	 * @param: [pkMessageId]
	 * @return: com.mpic.evolution.chair.pojo.vo.EcmInnerMessageVo
	 * @author: cxd
	 * @Date: 2020/9/26
	 * 描述 :	根据消息id获取消息
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	EcmInnerMessageVo getUserMsgByMsgId(Integer pkMessageId);
}
