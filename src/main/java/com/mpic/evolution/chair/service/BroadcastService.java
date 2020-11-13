package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;

/**
 * @author Administrator
 */
public interface BroadcastService {

    ResponseDTO getBroadcastInfo(EcmArtWorkQuery ecmArtWorkQuery);


    ResponseDTO getStorylineInfo(int intVideoId);

    void recordStorylineClick(int userId, int videoId, int passiveFlag, int currentPlayTime);

}
