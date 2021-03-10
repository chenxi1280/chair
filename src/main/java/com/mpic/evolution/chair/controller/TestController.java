package com.mpic.evolution.chair.controller;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.service.EcmVipService;
import com.mpic.evolution.chair.service.vip.BeanConfig;
import com.mpic.evolution.chair.service.vip.PaymentVipService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.util.RedisLock;

/**
* @author 作者 SJ:
* @date 创建时间：2020-10-8 17:12:42
*/

@Controller
@RequestMapping("/test")
public class TestController {

	@Resource
	private RedisLock redisLock;

	@Resource
	private EcmVipService ecmVipService;

	@Resource
	private BeanConfig beanConfig;

//	private int count = 0;

	/*@RequestMapping("/testRedisLock")
	@ResponseBody
	public void  testRedisLock() throws InterruptedException {
		int clientcount = 100000;
		CountDownLatch countDownLatch = new CountDownLatch(clientcount);
		ExecutorService threadPool = Executors.newFixedThreadPool(1000);
		long start = System.currentTimeMillis();
		for (int i = 0; i < clientcount; i++) {
			threadPool.execute(()->{
				String id = UUID.randomUUID().toString();
				try {
					redisLock.lock(id);
					count++;
					System.out.println("count值为："+ count);
				} finally {
					redisLock.unlock(id);
				}
				countDownLatch.countDown();
			});
		}
		countDownLatch.await();
		long end = System.currentTimeMillis();
		System.out.println(String.format("执行线程数:%d,总耗时:%d,count数为:%d",1000,end-start,count));
	}*/

	@RequestMapping("/testVip")
	@ResponseBody
	public ResponseDTO testVip() {
		JSONObject userVipInfo = ecmVipService.getUserVipInfo(1617);
		return ResponseDTO.ok(userVipInfo);
	}

	@RequestMapping("/testImpl")
	@ResponseBody
	public void testImpl(String type) {
		PaymentVipService updateVipDate = beanConfig.createQueryService(type);
		System.out.println(updateVipDate.toString());
//		updateVipDate.operationRelateToPayment(10);
	}

}
