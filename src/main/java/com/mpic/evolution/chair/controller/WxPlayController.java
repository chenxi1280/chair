package com.mpic.evolution.chair.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.service.WxPlayService;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-8 17:14:59 
*/
@Controller
@RequestMapping("/wxPlay")
public class WxPlayController {
	
	@Resource
    WxPlayService wxPlayService;
	
	/**
	 * @param: [ecmArtworkVo] 作品id
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/8
	 * 描述 : 小程序端
	 * 			根据作品id 查询作品详情 ，同时增加一次播放记录
	 *       成功: status 200  msg "success”
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/playArtWork")
	@ResponseBody
	public ResponseDTO playArtWork(@RequestBody EcmArtworkVo ecmArtworkVo){
		return wxPlayService.playArtWork(ecmArtworkVo);
	}
}
