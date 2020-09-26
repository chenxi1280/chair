package com.mpic.evolution.chair.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.mpic.evolution.chair.dao.*;
import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHistory;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHot;
import com.mpic.evolution.chair.pojo.entity.EcmReportHistroy;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.pojo.vo.WxPlayRecordVo;
import com.mpic.evolution.chair.pojo.vo.WxReportHistoryVo;
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
    
    @Resource
    EcmReportHistroyDao ecmReportHistroyDao;

	@Resource
	EcmArtworkDao ecmArtworkDao;
	/**
	 * 	根据artworkId 来查询作品树
	 * @param WxPlayRecordVo
	 * @return ResponseDTO
	 */
	@Override
    public ResponseDTO playArtWork(WxPlayRecordVo wxPlayRecordVo) {
        List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(wxPlayRecordVo.getPkArtworkId());
        if (list.isEmpty()) {
            return ResponseDTO.fail("查询id无子节点");
        }
        List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> !"Y".equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());
        return ResponseDTO.ok("success", TreeUtil.buildTree(collect).get(0));
    }
	
	/**
	 * 	每播放一次视频都要保存一次记录
	 * @param WxPlayRecordVo
	 * @return ResponseDTO
	 */
	@Override
    public ResponseDTO savaPlayRecord(WxPlayRecordVo wxPlayRecordVo) {
        EcmArtworkBroadcastHistory ecmArtworkBroadcastHistory = new EcmArtworkBroadcastHistory();
        ecmArtworkBroadcastHistory.setFkArtworkId(wxPlayRecordVo.getPkArtworkId());
        ecmArtworkBroadcastHistory.setStartTime(new Date());
        ecmArtworkBroadcastHistory.setFkUserId(wxPlayRecordVo.getUserId());
        ecmArtworkBroadcastHistory.setFkArtworkDetailId(wxPlayRecordVo.getDetailId());
        
        EcmArtworkBroadcastHot ecmArtworkBroadcastHot = new EcmArtworkBroadcastHot();
        ecmArtworkBroadcastHot.setFkArkworkId(wxPlayRecordVo.getPkArtworkId());
        try {
            ecmArtworkBroadcastHotDao.playArtWorkByArtworkId(wxPlayRecordVo.getPkArtworkId());
            ecmArtworkBroadcastHistoryDao.insertSelective(ecmArtworkBroadcastHistory);
        } catch (Exception e) {
        	e.printStackTrace();
        	return ResponseDTO.fail("保存播放记录失败");
        }
		return ResponseDTO.ok("保存播放记录成功");
	}
	
	/**
	 * 	根据用户传回的detailId 获取作品的树形结构
	 * @param WxPlayRecordVo
	 * @return ResponseDTO
	 */
	@Override
	public ResponseDTO playArtWorkByChildTree(WxPlayRecordVo wxPlayRecordVo) {
		List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(wxPlayRecordVo.getPkArtworkId());
        if (list.isEmpty()) {
            return ResponseDTO.fail("查询id无子节点");
        }
        List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> !"Y".equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());
        Integer detailId = wxPlayRecordVo.getDetailId();
        try {
            EcmArtworkNodesVo ecmArtworkNodesVo = TreeUtil.buildTreeByDetailId(collect, detailId).get(0);
            return ResponseDTO.ok("success", ecmArtworkNodesVo);
        }catch (IndexOutOfBoundsException e){

        }
        return ResponseDTO.fail("无节点数据");
	}
	

	/**
	 * 	保存用户投诉内容
	 * @param WxReportHistoryVo
	 * @return ResponseDTO
	 */
	@Override
	public ResponseDTO savaReportInfo(WxReportHistoryVo wxReportHistoryVo) {
		EcmReportHistroy reportHistroy = new EcmReportHistroy();
		reportHistroy.setContent(wxReportHistoryVo.getContent());
		reportHistroy.setImgUrl(wxReportHistoryVo.getImgUrl());
		reportHistroy.setReportStatue(wxReportHistoryVo.getReportStatue());
		reportHistroy.setFkArtworkId(wxReportHistoryVo.getFkArtworkId());
		reportHistroy.setFkArtworkNodeId(wxReportHistoryVo.getFkArtworkNodeId());
		reportHistroy.setFkUserid(wxReportHistoryVo.getFkUserid());
		reportHistroy.setCreatetime(new Date());
		reportHistroy.setReState((short)1);
		int row = ecmReportHistroyDao.insertSelective(reportHistroy); 
		if (row<1) {
			return ResponseDTO.fail("保存举报内容失败");
		}else {
			return ResponseDTO.ok("保存举报内容成功");
		}
	}

    @Override
    public ResponseDTO getUserIdByArtwordId(EcmArtWorkQuery ecmArtWorkQuery) {
		EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtWorkQuery.getPkArtworkId());
		return ResponseDTO.ok("success",ecmArtwork.getFkUserid());
    }
}
