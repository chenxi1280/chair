package com.mpic.evolution.chair.util;

import java.util.UUID;

/**
 * @author Administrator
 */
public class UUIDUtil {
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}
	
	public static void main(String[] args) {
		String uuid = UUIDUtil.getUUID();
		System.out.println(uuid);
	}
}
