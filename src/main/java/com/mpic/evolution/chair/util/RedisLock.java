package com.mpic.evolution.chair.util;

import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-10-8 12:46:30 
*/
@Component
public class RedisLock {
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	//锁过期时间
	private long lockInvalidTime = 3000;
	//获取锁超时时间
	private long timeout = 3600000;
	//锁键
	private String lock_key = "redis_lock";
	
	/**
	 * 	加锁
	 * @param id
	 * @return
	 */
	public boolean lock(String id) {
		long start = System.currentTimeMillis();
		try {
			for (;;) {
				String lua = "if redis.call('setNx',KEYS[1],ARGV[1]) then "
								+ "if redis.call('get',KEYS[1])==ARGV[1] then "
									+ "return redis.call('expire',KEYS[1],ARGV[2]) "
								+ "else "
									+ "return 0 "
								+ "end "
							+ "else "
								+ "return 0 "
							+ "end";
				RedisScript<Long> redisScript = new DefaultRedisScript<>(lua, Long .class);
				Long result = redisTemplate.execute(redisScript, Collections.singletonList(lock_key), 
						Collections.singleton(id),lockInvalidTime);
				if ("1".equals(result.toString())) {
					System.out.println("获得了锁");
					return true;
				}
				//否则循环等待，在timeout时间内仍未获得锁则获取锁失败
				long l =System.currentTimeMillis()-start;
				if(l>=timeout) {
					System.out.println("未解锁，获取不到锁");
					return false;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	/**
	 * 	解锁
	 * @param id
	 * @return
	 */
	public boolean unlock(String id) {
		String lua = "if redis.call('get',KEYS[1])==ARGV[1] then "
						+ "return redis.call('del',KEYS[1]) "
					 +"else "
						+ "return 0 "
					+ "end";
		RedisScript<Long> redisScript = new DefaultRedisScript<>(lua, Long.class);
		try {
			Long result = redisTemplate.execute(redisScript, Collections.singletonList(lock_key),
					Collections.singleton(id));
			if ("1".equals(result.toString())) {
				System.out.println("解锁");
				return true;
			}
			return false;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
}
