package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.dao.BroadcastDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.VideoBasicInfoVo;
import com.mpic.evolution.chair.service.BroadcastService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BroadcastServiceImpl  implements BroadcastService{
    @Autowired
    private BroadcastDao broadcastDao;

    @Override
    public ResponseDTO getBroadcastInfo(EcmArtWorkQuery ecmArtWorkQuery) {

        //在获得视频和子视频存储地址,id
        VideoBasicInfoVo vbInfo = broadcastDao.selectParentVideo(ecmArtWorkQuery.getIntVideoId());
        //插入一条播放记录
        broadcastDao.insertBroadcast(ecmArtWorkQuery);
        return ResponseDTO.ok("success", vbInfo);

    }


    /**
     *
     * 方法名:  getStorylineInfo
     * @author Xuezx
     * @date 2019/12/5 0005 14:25
     * @param videoId  视频id
     * 描述: 获得固始县信息
     */
    @Override
    public ResponseDTO getStorylineInfo(int videoId) {

        //首先查自己的作为第一个对象
        List<VideoBasicInfoVo> res = new ArrayList<>();
        VideoBasicInfoVo selfVo = broadcastDao.getStorylineInfoSelf(videoId);
        res.add(selfVo);
        int i = 0;
        //然后查父节点的,父节点不可能是结局
        VideoBasicInfoVo parentVo;
        int nowId = videoId;
        do {
            parentVo = broadcastDao.getStorylineInfo(nowId);
            if(parentVo != null && StringUtils.isNotBlank(parentVo.getItemsText())){
                nowId = parentVo.getVideoId();
                i++;
                res.add(parentVo);
            }
        } while (parentVo != null && StringUtils.isNotBlank(parentVo.getItemsText()));

        return ResponseDTO.ok("success", res);
    }


    /**
     *
     * 方法名:
     * @author Xuezx
     * @date 2020/6/11 0011 14:03
     * @param userId, videoId
     * @param passiveFlag 0 主动 1被动
     * 描述:
     */
    @Override
    public void recordStorylineClick(int userId, int videoId, int passiveFlag, int currentPlayTime){
        broadcastDao.recordStorylineClick(userId, videoId, passiveFlag, currentPlayTime);
    }
}
