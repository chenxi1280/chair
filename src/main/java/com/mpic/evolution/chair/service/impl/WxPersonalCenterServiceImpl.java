package com.mpic.evolution.chair.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
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
	
	@Override
	public ResponseDTO getWxUserArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {
		List<EcmArtworkVo> artworks = ecmArtworkDao.selectArtWorksByWxUser(ecmArtWorkQuery);
		artworks.sort((EcmArtworkVo a1,EcmArtworkVo a2)->a2.getLastModifyDate().compareTo(a1.getLastModifyDate()));
		Map<Short, List<EcmArtworkVo>> collect = artworks.stream().collect(Collectors.groupingBy(EcmArtworkVo::getArtworkStatus));
		if (collect.isEmpty()) {
			return ResponseDTO.ok("获取成功", null);
		}
		JSONObject data = new JSONObject();
		collect.forEach((k,v)->{
			if (k == 2) {
				data.put("verfied", collect.get(k));
			}
			if (k == 4) {
				data.put("published", collect.get(k));
			}
		});
		return ResponseDTO.ok("获取成功", data);
	}
}
