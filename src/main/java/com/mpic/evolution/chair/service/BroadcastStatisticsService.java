package com.mpic.evolution.chair.service;

import org.springframework.stereotype.Service;

import java.io.File;

/**
 *
 * 类名:  BroadcastStatisticsService
 * @author Xuezx
 * @date 2020/6/10 0010 14:53
 * 描述:
 */
@Service
public interface BroadcastStatisticsService {

    /**
     * 方法名:  getUserBroadcastInterested
     *
     * @author Xuezx
     * @date 2020/6/10 0010 14:53
     * 描述:
     */
    File getUserBroadcastInterested();

    File getUserBroadcastEnd();


    File getUserClickStoryLineTimes();

    File getUserClickStoryLineTimeVideoPart();

    void broadcastPageAction(int userId, int videoId, String actionType, int currentPlayTime);
}


