package com.mpic.evolution.chair.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.service.EcmArtWorkService;

@Controller
@RequestMapping("/ecmartWorkManager")
public class EcmArtWorkManagerController {
	@Resource
	EcmArtWorkService ecmArtWorkService;
	/**
	 * @author SJ
	 * @param ecmArtWorkQuery
	 * @return
	 */
	@RequestMapping("/modifyArtWorksStatus")
    @ResponseBody
    public ResponseDTO modifyArtWorksStatus(EcmArtWorkQuery ecmArtWorkQuery){
        return ecmArtWorkService.modifyArtWorkStatus(ecmArtWorkQuery);
    }
	
	/**
	 * @author SJ
	 * @param ecmArtWorkQuery
	 * @param code
	 * @return
	 */
	@RequestMapping("/modifyArtWorks")
    @ResponseBody
    public ResponseDTO modifyArtWorks(EcmArtWorkQuery ecmArtWorkQuery){
        return ecmArtWorkService.modifyArtWork(ecmArtWorkQuery);
    }
	
	/**
	 * @author SJ
	 * @param ecmArtworkVo
	 * @return
	 */
	@RequestMapping("/addArtWorks")
    @ResponseBody
    public ResponseDTO addArtWorks(EcmArtworkVo ecmArtworkVo){
        return ecmArtWorkService.addArtWorks(ecmArtworkVo);
    }
	
}
