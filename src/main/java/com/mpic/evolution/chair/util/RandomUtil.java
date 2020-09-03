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

	public static Integer getCode(int n) {
		Integer phoneConfirmCode = 1;
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			phoneConfirmCode += random.nextInt(10);
		}
		return phoneConfirmCode;
	}

	public static void main(String[] args) {
		System.out.println(getCode(2));
	}
}
