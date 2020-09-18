package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-18 16:41:17 
*/
public interface PersonalCenterService {

	ResponseDTO savaUserInfo(EcmUserVo ecmUserVo);

}
