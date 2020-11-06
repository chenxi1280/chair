package com.mpic.evolution.chair.service.impl;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.mpic.evolution.chair.common.constant.JudgeConstant;
import com.mpic.evolution.chair.dao.*;
import com.mpic.evolution.chair.pojo.entity.*;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.service.WxPlayService;
import com.mpic.evolution.chair.util.TreeUtil;
import org.springframework.util.CollectionUtils;

import static com.mpic.evolution.chair.util.VOUtils.setNodeNumberConditionFieldValue;
import static com.mpic.evolution.chair.util.VOUtils.setNodeNumberConditionValueFieldValue;

/**
 * @author 作者 SJ:
 * @date 创建时间：2020-9-9 10:39:39
 */

@Service
public class WxPlayServiceImpl implements WxPlayService {

    @Resource
    EcmArtworkNodesDao ecmArtworkNodesDao;

    @Resource
    EcmArtworkBroadcastHistoryDao ecmArtworkBroadcastHistoryDao;

    @Resource
    EcmArtworkBroadcastHotDao ecmArtworkBroadcastHotDao;

    @Resource
    EcmReportHistroyDao ecmReportHistroyDao;

    @Resource
    EcmArtworkNodeNumberConditionDao ecmArtworkNodeNumberConditionDao;

    @Resource
    EcmArtworkDao ecmArtworkDao;

    /**
     * 根据artworkId 来查询作品树
     *
     * @param WxPlayRecordVo
     * @return ResponseDTO
     */
    @Override
    public ResponseDTO playArtWork(WxPlayRecordVo wxPlayRecordVo) {
        List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(wxPlayRecordVo.getPkArtworkId());
        if (list.isEmpty()) {
            return ResponseDTO.fail("查询id无子节点");
        }
        List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> !JudgeConstant.Y.equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());
        return ResponseDTO.ok("success", TreeUtil.buildTree(collect).get(0));
    }

    /**
     * 每播放一次视频都要保存一次记录
     *
     * @param WxPlayRecordVo
     * @return ResponseDTO
     */
    @Override
    public ResponseDTO savaPlayRecord(WxPlayRecordVo wxPlayRecordVo) {
        EcmArtworkBroadcastHistory ecmArtworkBroadcastHistory = new EcmArtworkBroadcastHistory();
        ecmArtworkBroadcastHistory.setFkArtworkId(wxPlayRecordVo.getPkArtworkId());
        ecmArtworkBroadcastHistory.setStartTime(new Date());
        ecmArtworkBroadcastHistory.setFkUserId(wxPlayRecordVo.getUserId());
        ecmArtworkBroadcastHistory.setFkArtworkDetailId(wxPlayRecordVo.getDetailId());

        EcmArtworkBroadcastHot ecmArtworkBroadcastHot = new EcmArtworkBroadcastHot();
        ecmArtworkBroadcastHot.setFkArkworkId(wxPlayRecordVo.getPkArtworkId());
        try {
            ecmArtworkBroadcastHotDao.playArtWorkByArtworkId(wxPlayRecordVo.getPkArtworkId());
            ecmArtworkBroadcastHistoryDao.insertSelective(ecmArtworkBroadcastHistory);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.fail("保存播放记录失败");
        }
        return ResponseDTO.ok("保存播放记录成功");
    }

    /**
     * 根据用户传回的detailId 获取作品的树形结构
     *
     * @param WxPlayRecordVo
     * @return ResponseDTO
     */
    @Override
    public ResponseDTO playArtWorkByChildTree(WxPlayRecordVo wxPlayRecordVo) {
        List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(wxPlayRecordVo.getPkArtworkId());
        if (list.isEmpty()) {
            return ResponseDTO.fail("查询id无子节点");
        }
        List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> !JudgeConstant.Y.equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());
        List<EcmArtworkNodeNumberConditionVO> ecmArtworkNodeNumberConditionS = ecmArtworkNodeNumberConditionDao.selectByArtWorkId(wxPlayRecordVo.getPkArtworkId());
        Map<Integer, List<EcmArtworkNodeNumberConditionVO>> appearConditionMap = new HashMap<>();

        collect.forEach(v -> {
            // 还原定位数组方法
            if (!StringUtils.isEmpty(v.getItemsText())) {
                v.setNodeLocationList(JSON.parseArray(v.getItemsText(), NodeOptionLocationVO.class));
            }
            // 可优化
            // 还原数值数组方法
            if (!CollectionUtils.isEmpty(ecmArtworkNodeNumberConditionS)) {
                for (EcmArtworkNodeNumberCondition ecmArtworkNodeNumberCondition : ecmArtworkNodeNumberConditionS) {

                    if (ecmArtworkNodeNumberCondition.getPkDetailid().equals(v.getPkDetailId())) {
                        // ecmArtworkNodeNumberCondition中数值0 1 2 3，并改成 前端对应的 list<NodeNumberConditionVO
                        List<NodeNumberConditionVO> numberCondition = new ArrayList<>();
                        List<EcmArtworkNodeNumberConditionVO> ecmArtworkNodeNumberConditionList = new ArrayList<>();
                        Class<EcmArtworkNodeNumberCondition> ecmArtworkNodeNumberConditionClass = EcmArtworkNodeNumberCondition.class;
                        String appearCondition = "appearCondition";
                        String changeCondition = "changeCondition";
                        String changeConditionValue = "changeConditionValue";
                        for (int i = 0; i < 4; i++) {
                            NodeNumberConditionVO nodeNumberConditionVO = new NodeNumberConditionVO();
                            EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO = new EcmArtworkNodeNumberConditionVO();
                            setNodeNumberConditionFieldValue(ecmArtworkNodeNumberCondition, appearCondition, i, ecmArtworkNodeNumberConditionClass, nodeNumberConditionVO);
                            setNodeNumberConditionFieldValue(ecmArtworkNodeNumberCondition, changeCondition, i, ecmArtworkNodeNumberConditionClass, nodeNumberConditionVO);
//                          setNodeNumberConditionValueFieldValue(ecmArtworkNodeNumberCondition, ecmArtworkNodeNumberConditionClass, ecmArtworkNodeNumberConditionVOClass, changeCondition, changeConditionValue, i);
                            ecmArtworkNodeNumberConditionVO.setChangeConditionValue(Integer.valueOf(nodeNumberConditionVO.getChange() + nodeNumberConditionVO.getChangeValue()));
                            ecmArtworkNodeNumberConditionVO.setChangeFlag(ecmArtworkNodeNumberCondition.getChangeFlag());
//                            ecmArtworkNodeNumberConditionVO.setAppearFlag(ecmArtworkNodeNumberCondition.getAppearFlag());
//                            ecmArtworkNodeNumberConditionVO.setChangeFlag();
                            nodeNumberConditionVO.setChangeFlag(ecmArtworkNodeNumberCondition.getChangeFlag());
                            nodeNumberConditionVO.setAppearFlag(ecmArtworkNodeNumberCondition.getAppearFlag());
                            numberCondition.add(i, nodeNumberConditionVO);
                            ecmArtworkNodeNumberConditionList.add(i,ecmArtworkNodeNumberConditionVO);
                        }
                        appearConditionMap.put(v.getPkDetailId(),ecmArtworkNodeNumberConditionList);
                        v.setOnAdvancedList(numberCondition);
                        v.setChangeFlag(ecmArtworkNodeNumberCondition.getChangeFlag());
                        v.setAppearFlag(ecmArtworkNodeNumberCondition.getAppearFlag());
//                        v.setEcmArtworkNodeNumberCondition(ecmArtworkNodeNumberCondition);
//                        v.setChosenText(null);

//                        collect.forEach(vs -> {
//                            if (vs.getPkDetailId().equals(v.getParentId())) {
//                                vs.setChosenText("1");
//                            }
//                        });
                    }
                }
            }

        });


        Integer detailId = wxPlayRecordVo.getDetailId();
        try {
            EcmArtworkNodesVo ecmArtworkNodesVo = TreeUtil.buildTreeByDetailId(collect, detailId).get(0);
            ecmArtworkNodesVo.setAppearConditionMap(appearConditionMap);
            return ResponseDTO.ok("success", ecmArtworkNodesVo);
        } catch (IndexOutOfBoundsException e) {

        }
        return ResponseDTO.fail("无节点数据");
    }
    

    /**
     * 保存用户投诉内容
     *
     * @param WxReportHistoryVo
     * @return ResponseDTO
     */
    @Override
    public ResponseDTO savaReportInfo(WxReportHistoryVo wxReportHistoryVo) {
        EcmReportHistroy reportHistroy = new EcmReportHistroy();
        reportHistroy.setContent(wxReportHistoryVo.getContent());
        reportHistroy.setImgUrl(wxReportHistoryVo.getImgUrl());
        reportHistroy.setReportStatue(wxReportHistoryVo.getReportStatue());
        reportHistroy.setFkArtworkId(wxReportHistoryVo.getFkArtworkId());
        reportHistroy.setFkArtworkNodeId(wxReportHistoryVo.getFkArtworkNodeId());
        reportHistroy.setFkUserid(wxReportHistoryVo.getFkUserid());
        reportHistroy.setCreatetime(new Date());
        reportHistroy.setReState((short) 1);
        int row = 0;
        try{
            row +=  ecmReportHistroyDao.insertSelective(reportHistroy);
            EcmArtworkNodes ecmArtworkNodes = new EcmArtworkNodes();
            ecmArtworkNodes.setPkDetailId(reportHistroy.getFkArtworkNodeId());
            ecmArtworkNodes.setFkEndingId(6);
            row += ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodes);

        }catch (Exception e){

        }
        if (row < 2) {
            return ResponseDTO.fail("保存举报内容失败");
        } else {
            return ResponseDTO.ok("保存举报内容成功");
        }
    }

    @Override
    public ResponseDTO getUserIdByArtwordId(EcmArtWorkQuery ecmArtWorkQuery) {
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtWorkQuery.getPkArtworkId());
        return ResponseDTO.ok("success", ecmArtwork.getFkUserid());
    }
}
