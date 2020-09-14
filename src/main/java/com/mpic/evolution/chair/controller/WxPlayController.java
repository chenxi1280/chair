package com.mpic.evolution.chair.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.WxPlayRecordVo;
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
	 * @param: wxPlayRecordVo
	 * @return: ResponseDTO
	 * @author: SJ
	 * @Date: 2020/9/9
	 * 	描述 : 小程序端 根据作品id 查询作品树
	 */
	@RequestMapping("/playArtWork")
	@ResponseBody
	public ResponseDTO playArtWork(@RequestBody WxPlayRecordVo wxPlayRecordVo){
		return wxPlayService.playArtWork(wxPlayRecordVo);
		
	}
	
	/**
	 * @param: [ecmArtworkVo] 作品id
	 * @return: ResponseDTO
	 * @author: SJ
	 * @Date: 2020/9/9
	 * 	描述 : 小程序端播放一次视频 在history表中增加一次播放记录和在hot表中更行broadcastCount字段
	 */
	@RequestMapping("/savaPlayRecord")
	@ResponseBody
	public ResponseDTO savaPlayRecord(@RequestBody WxPlayRecordVo wxPlayRecordVo){
		return wxPlayService.savaPlayRecord(wxPlayRecordVo);
		
	}
	
	/**
	 * @param: wxPlayRecordVo
	 * @return: ResponseDTO
	 * @author: SJ
	 * @Date: 2020/9/9
	 * 	描述 : 小程序端 根据作品id 查询作品树再根据pkDetailId 筛选出子树
	 */
	@RequestMapping("/playArtWorkByChildTree")
	@ResponseBody
	public ResponseDTO playArtWorkByChildTree(@RequestBody WxPlayRecordVo wxPlayRecordVo){
		return wxPlayService.playArtWorkByChildTree(wxPlayRecordVo);
	}
	
	/**
	 * @param: wxPlayRecordVo
	 * @return: ResponseDTO
	 * @author: SJ
	 * @Date: 2020/9/9
	 * 	描述 : 小程序端 根据作品id 查询作品树再根据pkDetailId 筛选出子树
	 */
	@RequestMapping("/savaReportInfo")
	@ResponseBody
	public ResponseDTO savaReportInfo(@RequestBody WxPlayRecordVo wxPlayRecordVo){
		return wxPlayService.playArtWorkByChildTree(wxPlayRecordVo);
	}
}
