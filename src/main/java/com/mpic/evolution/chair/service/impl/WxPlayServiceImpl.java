package com.mpic.evolution.chair.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmArtworkBroadcastHistoryDao;
import com.mpic.evolution.chair.dao.EcmArtworkBroadcastHotDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHistory;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHot;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.service.WxPlayService;
import com.mpic.evolution.chair.util.TreeUtil;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-9 10:39:39 
*/

@Service
public class WxPlayServiceImpl implements WxPlayService {
	
	@Resource
	EcmArtworkNodesDao ecmArtworkNodesDao;
	 
	@Resource
    EcmArtworkBroadcastHistoryDao ecmArtworkBroadcastHistoryDao;
	
    @Resource
    EcmArtworkBroadcastHotDao ecmArtworkBroadcastHotDao;
    
	@Override
    public ResponseDTO playArtWork(EcmArtworkVo ecmArtworkVo) {
        List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(ecmArtworkVo.getPkArtworkId());
        if (list.isEmpty()) {
            return ResponseDTO.fail("查询id无子节点");
        }
        List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> !"Y".equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());
        //筛选出根节点的pk_detailId
        //需要添加 播放 历史表 数据
        EcmArtworkBroadcastHistory ecmArtworkBroadcastHistory = new EcmArtworkBroadcastHistory();
        ecmArtworkBroadcastHistory.setFkArtworkId(ecmArtworkVo.getPkArtworkId());
        ecmArtworkBroadcastHistory.setStartTime(new Date());

        EcmArtworkBroadcastHot ecmArtworkBroadcastHot = new EcmArtworkBroadcastHot();
        ecmArtworkBroadcastHot.setFkArkworkId(ecmArtworkVo.getPkArtworkId());
        try {
            ecmArtworkBroadcastHotDao.playArtWorkByArtworkId(ecmArtworkVo.getPkArtworkId());
            ecmArtworkBroadcastHistoryDao.insertSelective(ecmArtworkBroadcastHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseDTO.ok("success", TreeUtil.buildTree(collect).get(0));
    }
	
}
