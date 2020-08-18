package com.mpic.evolution.chair.dao;


import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.VideoBasicInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 类名:   BroadcastDao
 *
 * @author color
 * 播放记录接口 Dao
 */
@Component
public interface BroadcastDao {

    /**
     * getAchievementNums
     *
     * @param videoId 视频id（作品详情id）
     * @return int
     * 获得一个作品的url和类型信息
     */

    VideoBasicInfoVo selectParentVideo(@Param("videoId") int videoId);

    /**
     * 方法名: insertBroadcast
     *
     * @param userId  userId
     * @param videoId videoId
     *                描述: 插入播放记录一条
     * @author Xuezx
     * @date 2019/9/24 0024 8:42
     */

    void insertBroadcast(EcmArtWorkQuery ecmArtWorkQuery);


    VideoBasicInfoVo getStorylineInfoSelf(int videoId);

    VideoBasicInfoVo getStorylineInfo(int nowId);
}
