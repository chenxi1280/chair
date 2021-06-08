package com.mpic.evolution.chair.controller;

import javax.annotation.Resource;

import com.mpic.evolution.chair.pojo.vo.FreeAdVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.service.EcmArtworkManagerService;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("/ecmArtWorkManager")
public class EcmArtWorkManagerController extends BaseController {
	@Resource
	EcmArtworkManagerService artworkManagerService;
	
	/**
	 * @author SJ
	 * @param ecmArtworkVo
	 * @return
	 */
	@RequestMapping("/modifyArtWorksStatus")
    @ResponseBody
    public ResponseDTO modifyArtWorksStatus(@RequestBody EcmArtworkVo ecmArtworkVo){
		ecmArtworkVo.setFkUserid(getUserIdByHandToken());
        return artworkManagerService.modifyArtWorkStatus(ecmArtworkVo);
    }
	
	/**
	 * @author SJ
	 * @param ecmArtworkVo
	 * @return
	 */
	@RequestMapping("/modifyArtWorks")
    @ResponseBody
    public ResponseDTO modifyArtWorks(@RequestBody EcmArtworkVo ecmArtworkVo){
        return artworkManagerService.modifyArtWork(ecmArtworkVo);
    }
	
	/**
	 * @author SJ
	 * @param ecmArtworkVo
	 * @return
	 */
	
	@RequestMapping("/addArtWorks")
    @ResponseBody
    public ResponseDTO addArtWorks(@RequestBody EcmArtworkVo ecmArtworkVo){
        return artworkManagerService.addArtWorks(ecmArtworkVo);
    }

	/**
	 * @author SJ
	 * @param freeAdVo
	 * @return
	 */

	@RequestMapping("/checkFreeAd")
	@ResponseBody
	public ResponseDTO checkFreeAd(@RequestBody FreeAdVo freeAdVo){
		freeAdVo.setUserId(getUserIdByHandToken());
		return artworkManagerService.checkFreeAd(freeAdVo);
	}
	
}
