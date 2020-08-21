package com.mpic.evolution.chair.test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpic.evolution.chair.dao.EcmInnerMessageDao;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;

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
		ecmInnerMessage.setFkUserId(1352);
		List<EcmInnerMessage> messages = ecmInnerMessageDao.selectByList(ecmInnerMessage);
		messages = messages.stream().filter((EcmInnerMessage m)->m.getMessageStatus()<2).collect(Collectors.toList());
		messages.sort((EcmInnerMessage m1, EcmInnerMessage m2)->m2.getSendDate().compareTo(m1.getSendDate()));
		Map<Short, Long> sum = messages.stream().collect(Collectors.groupingBy(EcmInnerMessage::getMessageStatus, Collectors.counting()));
		sum.forEach((k,v)->System.out.println(k+":"+v));
	}

}
