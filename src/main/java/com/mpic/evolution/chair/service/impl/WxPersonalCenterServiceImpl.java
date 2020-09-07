package com.mpic.evolution.chair.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.service.WxPersonalCenterService;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-4 10:29:58 
*/

@Service
public class WxPersonalCenterServiceImpl implements WxPersonalCenterService {
	
	@Resource
    EcmArtworkDao ecmArtworkDao;
	
	@Resource
	EcmArtworkNodesDao ecmArtworkNodesDao;
	
	@Override
	public ResponseDTO getWxUserArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {
		List<EcmArtworkVo> artworks = ecmArtworkDao.selectArtWorksByWxUser(ecmArtWorkQuery);
		if (artworks.isEmpty()) {
			return ResponseDTO.ok("获取失败", null);
		}
		return ResponseDTO.ok("获取成功", artworks);
	}



	@Override
	public Integer getEcmArtworkRootNode(Integer pkArtworkId) {
		List<EcmArtworkNodesVo> artworkNodes = ecmArtworkNodesDao.selectByArtWorkId(103);
		List<EcmArtworkNodesVo> collect = artworkNodes.stream().filter(item->item.getParentId() == 0).distinct().collect(Collectors.toList());
		if (!collect.isEmpty()) {
			EcmArtworkNodesVo ecmArtworkNodesVo = collect.get(0);
			Integer pkDetailId = ecmArtworkNodesVo.getPkDetailId();
			return pkDetailId;
		}else {
			return null;
		}
	}
}
