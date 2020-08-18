package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;

public interface BroadcastService {

    ResponseDTO getBroadcastInfo(EcmArtWorkQuery ecmArtWorkQuery);


    ResponseDTO getStorylineInfo(int intVideoId);
}
