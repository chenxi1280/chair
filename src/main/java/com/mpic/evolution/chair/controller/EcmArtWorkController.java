package com.mpic.evolution.chair.controller;


import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
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
     * @param: [ecmArtWorkQuery] 传入的 查询参数 查询参数可以有 用户id，作品名称（模糊），视频状态，类型（当前模糊）
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 按照条件查询作品
     *       保存成功: status 200  msg "success” data: 数据
     *       保存失败: status 500  msg "error“
     */

    @RequestMapping("/getArtWorks")
    @ResponseBody
    public ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery){
        return ecmArtWorkService.getArtWorks(ecmArtWorkQuery);
    }



    /**
     * @param: [ecmArtWorkQuery] 需要 作品id 必传
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 查询作品详情根据 作品id
     *       保存成功: status 200  msg "success”  data: 数据
     *       保存失败: status 500  msg "error“
     */
    @RequestMapping("/getArtWork")
    @ResponseBody
    public ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery){

        return ecmArtWorkService.getArtWork(ecmArtWorkQuery);
    }

    /**
     * @param: [ecmArtworkNodes] 单个节点类
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 保存 作品单个节点 ArtWork 接口
     *       保存成功： status 200  msg “success”
     *       保存失败： status 500  msg ”error“
     */
    @ResponseBody
    @RequestMapping("/addArtWorkNode")
    public ResponseDTO addArtWorkNod(EcmArtworkNodes ecmArtworkNodes){
        return ecmArtWorkService.addArtWorkNode(ecmArtworkNodes);
    }


    /**
     * @param: [ecmArtworkNodesVo]  json 格式的 作品详情类（树状）
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 保存 作品(所有) ArtWork 接口
     *       保存成功: status 200  msg "success”
     *       保存失败: status 500  msg "error“
     */
    @RequestMapping("/addArtWork")
    @ResponseBody
    public ResponseDTO addArtWork(@RequestBody EcmArtworkNodesVo ecmArtworkNodesVo){
        return ecmArtWorkService.addArtWork(ecmArtworkNodesVo);
    }


}
