package com.mpic.evolution.chair.controller;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.service.EcmArtWorkService;

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
     * @param ecmArtWorkQuery 传入的 查询参数 查询参数可以有 用户id，作品名称（模糊），视频状态，类型（当前模糊）
     * @return ResponseDTO 中的data 包含 ArtWork的 条件查询 结果集
     */

    @RequestMapping("/getArtWorks")
    @ResponseBody
    public ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery){
        return ecmArtWorkService.getArtWorks(ecmArtWorkQuery);
    }




    @RequestMapping("/getArtWork")
    @ResponseBody
    public ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery){

        return ecmArtWorkService.getArtWork(ecmArtWorkQuery);
    }


}
