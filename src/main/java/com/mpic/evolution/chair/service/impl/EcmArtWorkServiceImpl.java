package com.mpic.evolution.chair.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.service.EcmArtWorkService;
import com.mpic.evolution.chair.util.TreeUtil;


@Service
public class EcmArtWorkServiceImpl implements EcmArtWorkService {
    @Resource
    EcmArtworkDao ecmArtworkDao;
    @Resource
    EcmArtworkNodesDao ecmArtworkNodesDao;

    @Override
    public ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {

        return   ResponseDTO.ok("success",ecmArtworkDao.selectArtWorks(ecmArtWorkQuery));
    }

    @Override
    public ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery) {
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtWorkQuery.getPkArtworkId());
        if (ecmArtwork==null){
            return ResponseDTO.fail("查询id为空");
        }
        List<EcmArtworkNodesVo>  list = ecmArtworkNodesDao.selectByArtWorkId(ecmArtwork.getPkArtworkId());
        return ResponseDTO.ok("success", TreeUtil.buildTree(list).get(0));
    } 

	@Override
	public ResponseDTO modifyArtWorkStatus(EcmArtWorkQuery ecmArtWorkQuery,String code) {
		JSONObject message = this.getMessage();
		try {
			JSONObject condition = this.getCondition();
			EcmArtwork ecmArtwork = new EcmArtwork();
			ecmArtwork.setPkArtworkId(ecmArtWorkQuery.getPkArtworkId());
			ecmArtwork.setArtworkStatus((short)condition.getInt(code));
			ecmArtwork.setFkUserid(ecmArtWorkQuery.getFkUserid());
			ecmArtwork.setArtworkName(ecmArtWorkQuery.getArtworkName());
			ecmArtworkDao.updateByPrimaryKey(ecmArtwork);   
			return ResponseDTO.ok(message.getString(code)+"成功");
		} catch (Exception e) {
			return ResponseDTO.fail(message.getString(code)+"失败", null, null, 500);
		}
		
	}

	@Override
	public ResponseDTO modifyArtWork(EcmArtWorkQuery ecmArtWorkQuery, String code) {
		try {
			EcmArtwork ecmArtwork = new EcmArtwork();
			ecmArtwork.setPkArtworkId(ecmArtWorkQuery.getPkArtworkId());
			ecmArtwork.setFkUserid(ecmArtWorkQuery.getFkUserid());
			if (code.equals("updateLogoPath")) ecmArtwork.setLogoPath(ecmArtWorkQuery.getLogoPath());
			ecmArtwork.setArtworkName(ecmArtWorkQuery.getArtworkName());				
			ecmArtworkDao.updateByPrimaryKey(ecmArtwork);   
			return ResponseDTO.ok("修改成功");
		} catch (Exception e) {
			return ResponseDTO.fail("修改失败", null, null, 500);
		}
	}
	
	private JSONObject getCondition() {
		JSONObject condition = new JSONObject();
		condition.put("delete", 5);
		condition.put("publish", 1);
		condition.put("cancel", 0);
		return condition;
	}
	
	private JSONObject getMessage() {
		JSONObject message = new JSONObject();
		message.put("delete", "删除");
		message.put("publish", "发布");
		message.put("cancel", "撤销审核");
		return message;
	}

	@Override
	public ResponseDTO addArtWorks(EcmArtworkVo ecmArtworkVo) {
		try {
			EcmArtwork ecmArtwork = new EcmArtwork();
			ecmArtwork.setPkArtworkId(ecmArtworkVo.getPkArtworkId());
			ecmArtwork.setFkUserid(ecmArtworkVo.getFkUserid());
			ecmArtwork.setArtworkName(ecmArtworkVo.getArtworkName());
			ecmArtwork.setArtworkStatus(ecmArtworkVo.getArtworkStatus());
			ecmArtwork.setArtworkDescribe(ecmArtworkVo.getArtworkDescribe());
			ecmArtwork.setFourLetterTips(ecmArtworkVo.getFourLetterTips());
			ecmArtwork.setLastCreateDate(ecmArtworkVo.getLastCreateDate());
			ecmArtwork.setLastModifyDate(ecmArtworkVo.getLastModifyDate());
			ecmArtwork.setLogoPath(ecmArtworkVo.getLogoPath());
			ecmArtworkDao.insert(ecmArtwork);
			return ResponseDTO.ok("新建成功");
		} catch (Exception e) {
			return ResponseDTO.fail("新建失败", null, null, 500);
		}
	}
}

