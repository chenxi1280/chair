package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.WxPlayRecordVo;
import com.mpic.evolution.chair.pojo.vo.WxReportHistoryVo;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-9 10:39:15 
*/
public interface WxPlayService {
	/**
     * @param: [ecmArtworkVo]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @Date: 2020/9/9
     * 	描述 :  根据作品id查询小程序播放作品的数据 并保存记录
     *      	 保存成功: status 200  msg "success” data "数据"
     *       	保存失败: status 500  msg "error“
     */
	ResponseDTO playArtWork(WxPlayRecordVo wxPlayRecordVo);
	
	ResponseDTO savaPlayRecord(WxPlayRecordVo wxPlayRecordVo);

	ResponseDTO playArtWorkByChildTree(WxPlayRecordVo wxPlayRecordVo);

	ResponseDTO savaReportInfo(WxReportHistoryVo wxReportHistoryVo);
}
