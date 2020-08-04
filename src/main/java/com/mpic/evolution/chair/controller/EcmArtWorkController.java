package com.mpic.evolution.chair.controller;


import com.mpic.evolution.chair.common.returnvo.ReturnVo;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.service.EcmArtWorkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @param ecmArtWorkQuery 传入的 查询参数 查询参数可以有 用户id，作品名称（模糊），视频状态，类型（当前模糊）
     * @return ResponseDTO 中的data 包含 ArtWork的 条件查询 结果集
     * @author: cxd
     * @Date: 2020/8/4
     */

    @RequestMapping("/getArtWorks")
    @ResponseBody
    public ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery){
        return ecmArtWorkService.getArtWorks(ecmArtWorkQuery);
    }



    /**
     * @param: [ecmArtWorkQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * 描述 : 查询作品详情根据 作品id
     * @author: cxd
     * @Date: 2020/8/4
     */
    @RequestMapping("/getArtWork")
    @ResponseBody
    public ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery){

        return ecmArtWorkService.getArtWork(ecmArtWorkQuery);
    }

    @ResponseBody
    @RequestMapping("/addArtWorkNode")
    public ResponseDTO addArtWorkNod(EcmArtworkNodes ecmArtworkNodes){


        return ecmArtWorkService.addArtWorkNode(ecmArtworkNodes);
    }

    @RequestMapping("/addArtWork")
    @ResponseBody
    public ResponseDTO addArtWork(@RequestBody EcmArtworkNodesVo ecmArtworkNodesVo){
        return ecmArtWorkService.addArtWork(ecmArtworkNodesVo);
    }


}
