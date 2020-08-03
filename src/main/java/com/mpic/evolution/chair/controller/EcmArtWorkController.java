package com.mpic.evolution.chair.controller;


import com.mpic.evolution.chair.common.returnvo.ReturnVo;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.service.EcmArtWorkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author cxd
 */
@Controller
@RequestMapping("/Ecmartwork")
public class EcmArtWorkController {

    @Resource
    EcmArtWorkService ecmArtWorkService;


    /**
     *
     * @param ecmArtWorkQuery 传入的 查询参数
     * @return ReturnVo 包含 ArtWork的 条件查询 结果集
     */

    @RequestMapping("/getArtWorks")
    @ResponseBody
    public ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery){
        return ecmArtWorkService.getArtWorks(ecmArtWorkQuery);
    }


}
