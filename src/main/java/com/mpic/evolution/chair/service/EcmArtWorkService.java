package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.common.returnvo.ReturnVo;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;

/**
 * @author Administrator
 */
public interface EcmArtWorkService {




    ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery);


}
