package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.vo.UserBroadcastInterestedVO;
import com.mpic.evolution.chair.pojo.vo.UserClickStoryLineTimeVideoPartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 类名:  BroadcastStatisticsDAO
 * @author Xuezx
 * @date 2020/6/12 0012 14:36
 * 描述:
 */
@Component
public interface BroadcastStatisticsDao {
    /**
     *
     * 方法名:  getUserBroadcastInterested
     * @author Xuezx
     * @date 2020/6/10 0010 14:55
     * @return java.util.List<com.mpic.evolution.forward.statistics.vo.UserBroadcastInterestedVO>
     * 描述:
     */
    List<UserBroadcastInterestedVO> getUserBroadcastInterested();

    /**
     *
     * 方法名:  getUserBroadcastInterestedLineTitles
     * @author Xuezx
     * @date 2020/6/10 0010 14:55
     * @return java.util.List<com.mpic.evolution.forward.statistics.vo.UserBroadcastInterestedVO>
     * 描述:
     */
    List<UserBroadcastInterestedVO> getUserBroadcastInterestedLineTitles();

    /**
     *
     * 方法名:  getUserBroadcastInterestedRowTitles
     * @author Xuezx
     * @date 2020/6/10 0010 14:55
     * @return java.util.List<com.mpic.evolution.forward.statistics.vo.UserBroadcastInterestedVO>
     * 描述:
     */
    List<UserBroadcastInterestedVO> getUserBroadcastInterestedRowTitles();

    List<UserBroadcastInterestedVO> getUserBroadcastEnd();

    List<UserBroadcastInterestedVO> getUserClickStoryLineTimes();

    List<UserClickStoryLineTimeVideoPartVO> getUserClickStoryLineTimeVideoPart();

    /**
     *
     * 方法名:
     * @author Xuezx
     * @date 2020/6/19 0019 16:17
     * @param userId,  userId
     * @param videoId,  videoId
     * @param actionType,  actionType
     * @param currentPlayTime currentPlayTime
     * 描述:
     */
    void broadcastPageAction(@Param("userId") int userId,
                             @Param("videoId") int videoId,
                             @Param("actionType") String actionType,
                             @Param("currentPlayTime") int currentPlayTime);
}
