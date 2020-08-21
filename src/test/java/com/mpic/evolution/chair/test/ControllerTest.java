package com.mpic.evolution.chair.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpic.evolution.chair.controller.LoginController;
import com.mpic.evolution.chair.service.LoginService;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-21 13:26:53 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest {
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	LoginController loginController;
	
	
}
