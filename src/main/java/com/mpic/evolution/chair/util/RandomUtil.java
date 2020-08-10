package com.mpic.evolution.chair.util;

import java.util.Random;

public class RandomUtil {
	public static String getCode() {
		String phoneConfirmCode = "";
		Random random = new Random();
        for (int i = 0; i < 6; i++) {
        	phoneConfirmCode += random.nextInt(10);
        }
		return phoneConfirmCode;
	}
}
