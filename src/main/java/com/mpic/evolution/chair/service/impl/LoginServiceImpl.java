package com.mpic.evolution.chair.service.impl;

import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	@Override
	public boolean isExsitWxUser(String openId, String unionId) {
		
		return false;
	}

	
}
