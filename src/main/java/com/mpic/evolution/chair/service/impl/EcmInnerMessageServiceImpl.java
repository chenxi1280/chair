package com.mpic.evolution.chair.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.dao.EcmOrderDao;
import com.mpic.evolution.chair.dao.EcmTemplateDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmOrder;
import com.mpic.evolution.chair.pojo.entity.EcmTemplate;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.query.EcmInnerMessageQurey;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;
import com.mpic.evolution.chair.util.Parser;
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
import org.springframework.transaction.annotation.TransactionManagementConfigurationSelector;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
* @author 作者 SJ:
* @date 创建时间：2020-8-20 10:50:58
*/
@Service
public class EcmInnerMessageServiceImpl implements EcmInnerMessageService{

	@Resource
	EcmInnerMessageDao ecmInnerMessageDao;
	@Resource
	EcmOrderDao ecmOrderDao;
	@Resource
	EcmTemplateDao ecmTemplateDao;



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
	public ResponseDTO batchDelete(EcmInnerMessageQurey ecmInnerMessageQurey) {
		try {
			Integer count = ecmInnerMessageDao.updateDelMsg(ecmInnerMessageQurey.getMessageIds(),ecmInnerMessageQurey.getPkUserId());
			if (count == ecmInnerMessageQurey.getMessageIds().size() && count !=1 ){
				return ResponseDTO.ok("操作成功");
			}else {
				return ResponseDTO.ok(null);
			}
		}catch (Exception e){
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResponseDTO.fail("网络错误");
		}
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResponseDTO batchModifyRead(EcmInnerMessageQurey ecmInnerMessageQurey) {
		try {
			if (CollectionUtils.isEmpty(ecmInnerMessageQurey.getMessageIds())){
				return ResponseDTO.ok(null);
			}
			Integer count = ecmInnerMessageDao.updateRedaMsg(ecmInnerMessageQurey.getMessageIds(),ecmInnerMessageQurey.getPkUserId());
			if (count == ecmInnerMessageQurey.getMessageIds().size() && count !=1 ){
				return ResponseDTO.ok("操作成功");
			}else {
				return ResponseDTO.ok(null);
			}
		}catch (Exception e){
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ResponseDTO.fail("网络错误");
		}
//		return ResponseDTO.fail("网络错误");
	}

	@Override
	public ResponseDTO getMsg(EcmUserVo user) {
		JSONObject data = new JSONObject();


		EcmInnerMessage ecmInnerMessage = new EcmInnerMessage();
		ecmInnerMessage.setFkUserId(user.getPkUserId());
		List<EcmInnerMessageVo> messages = ecmInnerMessageDao.selectByMsgList(ecmInnerMessage);

		if (messages == null || messages.isEmpty()) {
			return ResponseDTO.ok("无信息");
		}
		messages = messages.stream().filter((EcmInnerMessage m)->m.getMessageStatus()<2).collect(Collectors.toList());
		data.put("read", messages.size());
		messages.sort((EcmInnerMessage m1, EcmInnerMessage m2)->m2.getSendDate().compareTo(m1.getSendDate()));
		Map<Short, Long> sum = messages.stream().collect(Collectors.groupingBy(EcmInnerMessage::getMessageStatus, Collectors.counting()));

		data.put("messages", messages);
		data.put("unread", sum.get((short)0));
		return ResponseDTO.ok("获取成功", data);
	}

	@Override
	public EcmInnerMessageVo getUserMsgByMsgId(Integer pkMessageId) {
		return ecmInnerMessageDao.getUserMsgByMsgId(pkMessageId);
	}

    @Override
    public ResponseDTO sendMsgByOrder(EcmOrder ecmOrder) {

		EcmOrderVO ecmOrderVO = ecmOrderDao.selectOrderInfoByPrimaryKey(ecmOrder.getPkOrderId());
		EcmTemplate ecmTemplate = ecmTemplateDao.selectByPrimaryKey(19);

		EcmInnerMessageVo insertMsgVO = getInsertMsgVO(ecmTemplate.getContent(), ecmOrderVO);
		insertMsgVO.setFkTemplateId(ecmTemplate.getPkTemplateId());

		ecmInnerMessageDao.insertSelective(insertMsgVO);
		return ResponseDTO.ok();
    }

	// 站内信 msg 模板替换方法
	private EcmInnerMessageVo getInsertMsgVO(String content,EcmOrderVO ecmOrderVO){
		EcmInnerMessageVo ecmInnerMessageVO = new EcmInnerMessageVo();
		// ${} 替换名字
		ecmInnerMessageVO.setContent(Parser.parse0(content, ecmOrderVO.getUsername()));
//         !{} 替换作品
		ecmInnerMessageVO.setContent(Parser.parse("&{","}", ecmInnerMessageVO.getContent(),ecmOrderVO.getGoodsName() ));
//         @{}替换节点名字

		ecmInnerMessageVO.setMessageStatus((short) 0);
		ecmInnerMessageVO.setSendDate(new Date());
		ecmInnerMessageVO.setReviewerId(0);
		ecmInnerMessageVO.setFkUserId(ecmOrderVO.getFkUserId());
		return ecmInnerMessageVO;
	}

}
