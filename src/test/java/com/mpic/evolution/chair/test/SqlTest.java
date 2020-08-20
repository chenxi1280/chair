package com.mpic.evolution.chair.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONArray;
import com.mpic.evolution.chair.dao.EcmInnerMessageDao;
import com.mpic.evolution.chair.pojo.dto.EcmInnerMessageDto;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;

import net.minidev.json.JSONObject;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 10:17:08 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SqlTest {
	@Autowired
	EcmInnerMessageDao ecmInnerMessageDao;
	
	@Test
	public void selectSqlTest() {
		EcmInnerMessage ecmInnerMessage = new EcmInnerMessage();
		ecmInnerMessage.setFkTemplateId(1);
//		ecmInnerMessage.setFkUserId(1352);
		List<EcmInnerMessageDto> selectByList = ecmInnerMessageDao.selectByList(ecmInnerMessage);
		JSONArray data = new JSONArray();
		selectByList.forEach(intem->{
			//键一样问题
			JSONObject object = new JSONObject();
			String content = intem.getEcmTemplate().getContent();
			Short status = intem.getMessageStatus();
			object.put("content", content);
			object.put("status", status);
			data.add(object);
		});
		System.out.println();
	}

}
