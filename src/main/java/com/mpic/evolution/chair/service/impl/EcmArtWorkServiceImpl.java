package com.mpic.evolution.chair.service.impl;

import com.alibaba.fastjson.JSON;
import com.mpic.evolution.chair.common.constant.JudgeConstant;
import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.dao.*;
import com.mpic.evolution.chair.pojo.dto.EcmArtworkNodesDTO;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.*;
import com.mpic.evolution.chair.pojo.query.*;
import com.mpic.evolution.chair.pojo.vo.*;
import com.mpic.evolution.chair.service.EcmArtWorkService;
import com.mpic.evolution.chair.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.mpic.evolution.chair.common.constant.CommonField.*;
import static com.mpic.evolution.chair.common.constant.JudgeConstant.SUCCESS;
import static com.mpic.evolution.chair.common.constant.JudgeConstant.Y;


/**
 * @author cxd
 */
@Service
public class EcmArtWorkServiceImpl implements EcmArtWorkService {
    @Resource
    EcmArtworkDao ecmArtworkDao;
    @Resource
    EcmUserDao ecmUserDao;
    @Resource
    EcmArtworkNodesDao ecmArtworkNodesDao;
    @Resource
    ProcessMediaByProcedureDao processMediaByProcedureDao;
    @Resource
    EcmArtworkBroadcastHotDao ecmArtworkBroadcastHotDao;
    @Resource
    EcmArtworkNodeNumberConditionDao ecmArtworkNodeNumberConditionDao;
    @Resource
    EcmArtworkEndingsDao ecmArtworkEndingsDao;
    @Resource
    EcmArtworkNodePopupSettingsDao ecmArtworkNodePopupSettingsDao;
    @Resource
    EcmArtworkNodeBuoyDao ecmArtworkNodeBuoyDao;
    @Resource
    EcmArtworkNodeActionDao ecmArtworkNodeActionDao;
    @Resource
    EcmArtworkNodeBuoyPanoramicDao ecmArtworkNodeBuoyPanoramicDao;
    @Resource
    EcmArtworkFreeAdDao ecmArtworkFreeAdDao;

    @Override
    public ResponseDTO updateNodeInfo() {
        List<EcmArtworkVo> ecmArtworkVoList = ecmArtworkDao.selectArtWorksAll();
        List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkList(ecmArtworkVoList);
        List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> {
            if (Y.equals(ecmArtworkNodesVo.getIsDeleted())) {
                return false;
            }
            if (ecmArtworkNodesVo.getALevel() != null && ecmArtworkNodesVo.getALevel().equals(1)) {
                return false;
            }
            if (ecmArtworkNodesVo.getVideoUrl() == null) {
                return false;
            }
            if (ecmArtworkNodesVo.getFkArtworkId() == null) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        collect.forEach(v -> {
            ecmArtworkVoList.forEach(addArtWork -> {
                if (v.getFkArtworkId().equals(addArtWork.getPkArtworkId())) {
                    v.setPlayMode(addArtWork.getPlayMode());
                    if (addArtWork.getPlayMode() == null) {
                        v.setPlayMode(0);
                    }
                }
            });
        });

        return ResponseDTO.ok(collect);
    }


    @Override
    public void insertNodeInfo(EcmArtworkNodesVo ecmArtworkNodesVo) {
        EcmArtworkNodes ecmArtworkNodes = new EcmArtworkNodes();
        ecmArtworkNodes.setPkDetailId(ecmArtworkNodesVo.getPkDetailId());
        ecmArtworkNodes.setVideoInfo(ecmArtworkNodesVo.getVideoInfo());
        ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodes);
    }


    @Override
    public ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {
        List<EcmArtworkVo> ecmArtworkVoList = ecmArtworkDao.selectArtWorkListByEcmArtWorkQuery(ecmArtWorkQuery);
        Integer count = ecmArtworkDao.selectArtWorkCountByEcmArtWorkQuery(ecmArtWorkQuery);
        // ??????
        List<EcmArtworkVo> collect = ecmArtworkVoList.stream().filter(ecmArtworkVo -> ecmArtworkVo.getArtworkStatus() == 4).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            List<EcmArtworkBroadcastHotVO> list = ecmArtworkBroadcastHotDao.selectEcmArtworkList(collect);
            ecmArtworkVoList.forEach(ecmArtworkVo -> {
                list.forEach(ecmArtworkBroadcastHotVO -> {
                    if (ecmArtworkBroadcastHotVO.getFkArkworkId().equals(ecmArtworkVo.getPkArtworkId())) {
                        ecmArtworkVo.setHotCount(ecmArtworkBroadcastHotVO.getBroadcastCount());
                    }
                });
                if (ecmArtworkVo.getHotCount() == null) {
                    ecmArtworkVo.setHotCount(0);
                }
            });
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("list", ecmArtworkVoList);
        map.put("count", count);
        return ResponseDTO.ok("success", map);
    }

    @Override
    public ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery) {
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtWorkQuery.getPkArtworkId());
        if (ecmArtwork == null) {
            return ResponseDTO.fail(ErrorEnum.ERR_003.getText());
        }
        if (!ecmArtWorkQuery.getFkUserid().equals(ecmArtwork.getFkUserid())) {
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        // ?????? ???????????????
        List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(ecmArtWorkQuery.getPkArtworkId());
        // ???????????????  ??????
        List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> !JudgeConstant.Y.equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(collect)) {
            return ResponseDTO.ok(ErrorEnum.ERR_601.getText());
        }
        // ??????????????????????????????
        List<EcmArtworkNodeNumberConditionVO> ecmArtworkNodeNumberConditionS = ecmArtworkNodeNumberConditionDao.selectByArtWorkId(ecmArtWorkQuery.getPkArtworkId());
        // ????????? ?????????  ??????????????????
        List<EcmArtworkNodePopupSettingsVO> ecmArtworkNodePopupSettingsVOList = ecmArtworkNodePopupSettingsDao.selectByArtworkNodeList(collect);
        //????????? ?????? ??????????????? ??????
        List<EcmArtworkNodesVo> filterBuoyList = collect.stream().filter(ecmArtworkNodesVo -> BYTER_TWO.equals(ecmArtworkNodesVo.getBranchPre())).collect(Collectors.toList());
        // ???????????? ???????????????
        List<EcmArtworkNodeBuoyVO> ecmArtworkNodeBuoyVOList;
        Map<Integer, List<EcmArtworkNodeBuoyVO>> map = new HashMap<>(INT_ONE);
        if (!CollectionUtils.isEmpty(filterBuoyList)) {
            ecmArtworkNodeBuoyVOList = ecmArtworkNodeBuoyDao.selectByEcmArtworkNodeList(filterBuoyList);
            // ?????? nodeID ????????????
            map = ecmArtworkNodeBuoyVOList.stream().collect(Collectors.groupingBy(EcmArtworkNodeBuoy::getFkNodeId));
        }

        //??????????????????????????????
        EcmArtworkFreeAd ecmArtworkFreeAd = new EcmArtworkFreeAd();
        ecmArtworkFreeAd.setFkArtworkId(ecmArtWorkQuery.getPkArtworkId());
        ecmArtworkFreeAd = ecmArtworkFreeAdDao.selectByRecord(ecmArtworkFreeAd);

        //?????? ???????????????  ????????????
        for (EcmArtworkNodesVo node : collect) {
            // ?????????????????????
            if (!StringUtils.isEmpty(node.getItems()) && node.getALevel() != null) {
                if (node.getALevel().equals(INT_ONE)) {
                    for (EcmArtworkNodesVo ecmArtworkNodesVo : list) {
                        if (!JudgeConstant.Y.equals(ecmArtworkNodesVo.getIsDeleted())) {
                            if (ecmArtworkNodesVo.getPkDetailId().equals(Integer.valueOf(node.getItems()))) {
                                if (JudgeConstant.Y.equals(ecmArtworkNodesVo.getIsDeleted())) {
                                    node.setItems(null);
                                } else {
                                    EcmArtworkNodesDTO ecmArtworkNodesDTO = new EcmArtworkNodesDTO();
                                    BeanUtils.copyProperties(ecmArtworkNodesVo, ecmArtworkNodesDTO);
                                    ecmArtworkNodesDTO.setFkArtworkId(ecmArtWorkQuery.getPkArtworkId());
                                    node.setLinkNode(ecmArtworkNodesDTO);
                                }
                            }
                        }
                    }
                }
            }
            // ????????????????????????
            if (node.getBranchPre() != null && node.getBranchPre() == BYTE_ONE) {
                if (!StringUtils.isEmpty(node.getItemsText())) {
                    node.setNodeLocationList(JSON.parseArray(node.getItemsText(), NodeOptionLocationVO.class));
                    node.setItemsText(null);
                }
            }
            // ????????????
            if (node.getBranchPre() != null && node.getBranchPre() == BYTE_TWO) {
                if (!CollectionUtils.isEmpty(map)) {
                    node.setEcmArtworkNodeBuoyVOList(map.get(node.getPkDetailId()));
                }
            }
            // ????????????????????????
            if (!CollectionUtils.isEmpty(ecmArtworkNodeNumberConditionS)) {
                for (EcmArtworkNodeNumberCondition ecmArtworkNodeNumberCondition : ecmArtworkNodeNumberConditionS) {
                    if (ecmArtworkNodeNumberCondition.getPkDetailid().equals(node.getPkDetailId())) {
                        // ecmArtworkNodeNumberCondition?????????0 1 2 3???????????? ??????????????? list<NodeNumberConditionVO
                        List<NodeNumberConditionVO> numberCondition = new ArrayList<>(5);
                        List<String> names = new ArrayList<>(5);
//                        List<Boolean> allNodeNameFlagList = new ArrayList<>(4);
                        Class<EcmArtworkNodeNumberCondition> ecmArtworkNodeNumberConditionClass = EcmArtworkNodeNumberCondition.class;
                        String appearCondition = "appearCondition";
                        String changeCondition = "changeCondition";
                        String nameCondition = "nameCondition";
                        String nameDisplay = "nameDisplay";
                        for (int i = 0; i < 5; i++) {
                            NodeNumberConditionVO nodeNumberConditionVO = new NodeNumberConditionVO();
                            //????????????????????????
                            VOUtils.setNodeNumberConditionFieldValue(ecmArtworkNodeNumberCondition, appearCondition, i, ecmArtworkNodeNumberConditionClass, nodeNumberConditionVO);
                            VOUtils.setNodeNumberConditionFieldValue(ecmArtworkNodeNumberCondition, changeCondition, i, ecmArtworkNodeNumberConditionClass, nodeNumberConditionVO);
                            VOUtils.setNodeNumberConditionFieldValue(ecmArtworkNodeNumberCondition, nameCondition, i, ecmArtworkNodeNumberConditionClass, nodeNumberConditionVO);
                            VOUtils.setNodeNumberConditionFieldValue(ecmArtworkNodeNumberCondition, nameDisplay, i, ecmArtworkNodeNumberConditionClass, nodeNumberConditionVO);
                            //?????? ?????????flag
                            nodeNumberConditionVO.setChangeFlag(ecmArtworkNodeNumberCondition.getChangeFlag());
                            nodeNumberConditionVO.setAppearFlag(ecmArtworkNodeNumberCondition.getAppearFlag());
                            nodeNumberConditionVO.setNameFlag(ecmArtworkNodeNumberCondition.getNameFlag());
                            names.add(nodeNumberConditionVO.getNameCondition());
                            numberCondition.add(i, nodeNumberConditionVO);
                        }
                        node.setOnAdvancedList(numberCondition);
                        node.setEcmArtworkNodeNumberCondition(ecmArtworkNodeNumberCondition);
                        list.get(0).setOnNameConditionList(names);
                        if (ecmArtworkNodeNumberCondition.getAllNameFlag() != null) {
                            node.setAllNameFlag(ecmArtworkNodeNumberCondition.getAllNameFlag());
                        }
                    }
                }
            }
//             ?????? ????????????
            if (!CollectionUtils.isEmpty(ecmArtworkNodePopupSettingsVOList)) {
                ecmArtworkNodePopupSettingsVOList.forEach(ecmArtworkNodePopupSettingsVO -> {
                    if (ecmArtworkNodePopupSettingsVO.getFkNodeId().equals(node.getPkDetailId())) {
                        ecmArtworkNodePopupSettingsVO.setPopupState(node.getPopupState());
                        node.setEcmArtworkNodePopupSettings(ecmArtworkNodePopupSettingsVO);
                    }
                });
            }
            // ??? ?????? ??? ???????????? ??? null ??? ????????? 0 ??????????????????
            if (node.getPopupState() == null) {
                node.setPopupState(INT_ZORE);
            }
            if (node.getConditionState() == null) {
                node.setConditionState(INT_ZORE);
            }

            if (node.getParentId().equals(INT_ZORE)){
                if(ecmArtworkFreeAd == null){
                    node.setPlayType(0);
                }else{
                    node.setPlayType(1);
                }
            }

        }

        // ????????????
        collect.get(INT_ZORE).setArtWorkTips(ecmArtwork.getFourLetterTips());
        // ??????????????????
        collect.get(INT_ZORE).setPlayMode(ecmArtwork.getPlayMode() == null ? INT_ZORE : ecmArtwork.getPlayMode());
        // ??????????????? ??????
        collect.get(INT_ZORE).setIsEndings(INT_ZORE);
        if (ecmArtwork.getIsEndings() != null) {
            collect.get(INT_ZORE).setIsEndings(ecmArtwork.getIsEndings());
        }
        List<EcmArtworkEndingsVO> ecmArtworkEndingsVOList = ecmArtworkEndingsDao.selectByArtwId(ecmArtwork.getPkArtworkId());

        // ???????????? ??? ???????????????
        collect.get(INT_ZORE).setEndingCount(ecmArtworkEndingsVOList.size());
        if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOList)) {
            collect.get(INT_ZORE).setEndingConditionState(ecmArtworkEndingsVOList.get(INT_ZORE).getConditionState());
        } else {
            collect.get(INT_ZORE).setEndingConditionState(INT_ZORE);
        }

        //???????????????????????? ??????
        if (!CollectionUtils.isEmpty(ecmArtworkNodePopupSettingsVOList)) {
            collect.get(INT_ZORE).setPopupGlobalName(ecmArtworkNodePopupSettingsVOList.get(INT_ZORE).getPopupName());
            collect.get(INT_ZORE).setPopupGlobalNameState(ecmArtworkNodePopupSettingsVOList.get(INT_ZORE).getPopupNameState());
        }

        // ???????????? ????????? 1
        collect.get(INT_ZORE).setPercentageState(collect.get(INT_ZORE).getPercentageState() == null ? INT_ONE : collect.get(INT_ZORE).getPercentageState());
        return ResponseDTO.ok("success", TreeUtil.buildTree(collect).get(INT_ZORE));
    }


    @Override
    public ResponseDTO saveArtWorkNode(EcmArtworkNodesVo ecmArtworkNodes) {

        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodes.getFkArtworkId());
        if (!ecmArtwork.getFkUserid().equals(ecmArtworkNodes.getFkUserId())) {
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        String videoCode = ecmArtworkNodes.getVideoCode();
        if (StringUtils.isNotBlank(videoCode)) {
            MediaByProcedureVo vo = new MediaByProcedureVo();
            vo.setVideoCode(videoCode);
            List<MediaByProcedureVo> list = processMediaByProcedureDao.getUnHandledVideoByVideoCode(vo);
            if (list == null || list.size() <= 0) {
                vo.setArtworkId(ecmArtworkNodes.getFkArtworkId());
                vo.setVideoId(ecmArtworkNodes.getPkDetailId());
                processMediaByProcedureDao.insertUnHandledVideo(vo);
            }
        }
        //??????????????????
        if (JudgeConstant.Y.equals(ecmArtworkNodes.getIsleaf())) {
            ecmArtworkNodes.setIsleaf("");
            ecmArtworkNodes.setFkEndingId(10);
            ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodes);

            // ?????????????????????
            if (!StringUtils.isEmpty(ecmArtworkNodes.getItems())) {
                EcmArtworkNodes ecmArtworkNode = ecmArtworkNodesDao.selectByPrimaryKey(Integer.valueOf(ecmArtworkNodes.getItems()));
                EcmArtworkNodesDTO ecmArtworkNodesDTO = new EcmArtworkNodesDTO();

                BeanUtils.copyProperties(ecmArtworkNode, ecmArtworkNodesDTO);
                ecmArtworkNodes.setLinkNode(ecmArtworkNodesDTO);
            }



            return ResponseDTO.ok("??????", ecmArtworkNodes);
        }
        //??????????????????
        if (!ecmArtworkNodes.getALevel().equals(1) && ecmArtworkNodes.getALevel() != null) {
            ecmArtworkNodes.setItemsBakText("https://sike-1259692143.cos.ap-chongqing.myqcloud.com/img/1604281276527nodeImgUrl.png");
        }
        if (StringUtils.isEmpty(ecmArtworkNodes.getCssVo())) {
            ecmArtworkNodes.setCssVo("????????????????????????\n??????15???");
        }
        if (StringUtils.isEmpty(ecmArtworkNodes.getVideoText())) {
            ecmArtworkNodes.setVideoText("???????????????");
        }
        if (ecmArtworkNodes.getBranchPre() == null) {
            ecmArtworkNodes.setBranchPre((byte) 0);
        }
        if (1 == ecmArtworkNodesDao.insertSelective(ecmArtworkNodes)) {
            return ResponseDTO.ok("success", ecmArtworkNodes);
        }
        return ResponseDTO.fail("?????????????????????????????????");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO addArtWork(EcmArtworkNodesVo ecmArtworkNodesVo) {
        // ??? saveArtwork ??? ?????? ????????? ???????????????????????? ???????????????
        try {
            saveArtwork(ecmArtworkNodesVo);
        } catch (Exception e) {
            e.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseDTO.fail("error");
        }

        return ResponseDTO.ok("success");
    }

    @Override
    public ResponseDTO removeNode(EcmArtworkNodesVo ecmArtworkNodesVo) {
        if (ecmArtworkNodesVo.getFkArtworkId() == null) {
            return ResponseDTO.fail("????????????");
        }
        if (ecmArtworkNodesVo.getPkDetailId() == null) {

            return ResponseDTO.fail("????????????");
        }
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodesVo.getFkArtworkId());
        if (!ecmArtwork.getFkUserid().equals(ecmArtworkNodesVo.getFkUserId())) {
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        List<EcmArtworkNodesVo> ecmArtworkNodesVoList = ecmArtworkNodesDao.selectByArtWorkId(ecmArtworkNodesVo.getFkArtworkId());
        List<Integer> ids = new ArrayList<>();
        ids.add(ecmArtworkNodesVo.getPkDetailId());
        getRemoveNodeId(ecmArtworkNodesVo, ecmArtworkNodesVoList, ids);
        ecmArtworkNodesDao.removeByNodeIds(ids);
        return ResponseDTO.ok();
    }

    private void getRemoveNodeId(EcmArtworkNodesVo ecmArtworkNodesVo, List<EcmArtworkNodesVo> ecmArtworkNodesVoList, List<Integer> ids) {
        ecmArtworkNodesVoList.forEach(v -> {
            if (v.getParentId().equals(ecmArtworkNodesVo.getPkDetailId())) {
                ids.add(v.getPkDetailId());
                getRemoveNodeId(v, ecmArtworkNodesVoList, ids);
            }
        });
    }

    @Override
    public ResponseDTO saveArtworkNodeNumberCondition(EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO) {
        EcmArtworkNodes ecmArtworkNodes = ecmArtworkNodesDao.selectByPrimaryKey(ecmArtworkNodeNumberConditionVO.getPkDetailid());
        if (ecmArtworkNodes == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkNodes.getFkArtworkId().equals(ecmArtworkNodeNumberConditionVO.getFkArtworkId())) {
            return ResponseDTO.fail("????????????");
        }
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodes.getFkArtworkId());
        if (!ecmArtwork.getFkUserid().equals(ecmArtworkNodeNumberConditionVO.getFkUserId())) {
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }

        EcmArtworkNodeNumberCondition ecmArtworkNodeNumberCondition = ecmArtworkNodeNumberConditionDao.selectByPrimaryKey(ecmArtworkNodeNumberConditionVO.getPkDetailid());

        if (ecmArtworkNodeNumberConditionVO.getAllNameFlag() == null) {
            if (ecmArtworkNodeNumberConditionVO.getAppearFlag() != null || ecmArtworkNodeNumberConditionVO.getChangeFlag() != null) {
                if (ecmArtworkNodeNumberConditionVO.getAppearFlag() == 1 || ecmArtworkNodeNumberConditionVO.getChangeFlag() == 1) {
                    ecmArtworkNodes.setChosenText("1");
                } else {
                    ecmArtworkNodes.setChosenText("0");
                }
            }

            if (ecmArtworkNodeNumberConditionVO.getNameFlag() != null) {
                ecmArtworkNodes.setChosenText("1");
            }
        }

        ecmArtworkNodeNumberConditionVO.setUpdataDate(new Date());
        ecmArtworkNodeNumberConditionVO.setNameFlag((byte) 1);
        try {
            if (ecmArtworkNodeNumberConditionVO.getAllNodeNameFlag() != null) {
                if (ecmArtworkNodeNumberConditionVO.getAllNodeNameFlag()) {
                    ecmArtworkNodeNumberConditionDao.updateNameConditionNameFLagByArtworkID(ecmArtworkNodeNumberConditionVO);
                    ecmArtworkNodesDao.updateNodeNumberFlag(ecmArtworkNodes.getFkArtworkId());
                    List<EcmArtworkNodeNumberConditionVO> ecmArtworkNodeNumberConditionVOList = ecmArtworkNodeNumberConditionDao.selectByArtWorkId(ecmArtworkNodeNumberConditionVO.getFkArtworkId());
                    List<EcmArtworkNodesVo> ecmArtworkNodesVoList = ecmArtworkNodesDao.selectByArtWorkId(ecmArtworkNodeNumberConditionVO.getFkArtworkId());
                    if (!CollectionUtils.isEmpty(ecmArtworkNodeNumberConditionVOList) && !CollectionUtils.isEmpty(ecmArtworkNodesVoList)) {
                        if (ecmArtworkNodeNumberConditionVOList.size() == ecmArtworkNodesVoList.size()) {
                            return ResponseDTO.ok("??????????????????");
                        }
                    }
                    Iterator<EcmArtworkNodesVo> iterator = ecmArtworkNodesVoList.iterator();
                    while (iterator.hasNext()) {
                        EcmArtworkNodesVo ecmArtworkNodesVo = iterator.next();
                        if (!CollectionUtils.isEmpty(ecmArtworkNodeNumberConditionVOList)) {
                            for (EcmArtworkNodeNumberConditionVO ecmArtworkNode : ecmArtworkNodeNumberConditionVOList) {
                                if (ecmArtworkNodesVo.getPkDetailId().equals(ecmArtworkNode.getPkDetailid())) {
                                    iterator.remove();
                                }
                            }
                        }
                    }
                    List<EcmArtworkNodeNumberConditionVO> ecmArtworkNodeNumberList = new ArrayList<>();
                    ecmArtworkNodesVoList.forEach(v -> {
                        EcmArtworkNodeNumberConditionVO artworkNodeNumberConditionVO = new EcmArtworkNodeNumberConditionVO();
                        BeanUtils.copyProperties(ecmArtworkNodeNumberConditionVO, artworkNodeNumberConditionVO);
                        artworkNodeNumberConditionVO.setPkDetailid(v.getPkDetailId());
                        artworkNodeNumberConditionVO.setCreateDate(new Date());
                        ecmArtworkNodeNumberList.add(artworkNodeNumberConditionVO);
                    });
                    if (!CollectionUtils.isEmpty(ecmArtworkNodeNumberList)) {
                        ecmArtworkNodeNumberConditionDao.insertList(ecmArtworkNodeNumberList);
                    }
//                    ecmArtworkNodeNumberConditionDao.insertList();
                    return ResponseDTO.ok("??????????????????");
                }
            }
            ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodes);
            ecmArtworkNodeNumberConditionDao.updateNameConditionByArtworkID(ecmArtworkNodeNumberConditionVO);

            if (ecmArtworkNodeNumberCondition != null) {
                ecmArtworkNodeNumberConditionDao.updateByPrimaryKeySelective(ecmArtworkNodeNumberConditionVO);
                return ResponseDTO.ok();
            }

            ecmArtworkNodeNumberConditionVO.setCreateDate(new Date());
            ecmArtworkNodeNumberConditionDao.insertNode(ecmArtworkNodeNumberConditionVO);
            return ResponseDTO.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.fail("????????????");
        }
    }

    @Override
    public ResponseDTO saveAllNodeNameFlagChange(EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO) {
        EcmArtworkNodes ecmArtworkNodes = ecmArtworkNodesDao.selectByPrimaryKey(ecmArtworkNodeNumberConditionVO.getPkDetailid());
        if (ecmArtworkNodes == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkNodes.getFkArtworkId().equals(ecmArtworkNodeNumberConditionVO.getFkArtworkId())) {
            return ResponseDTO.fail("????????????");
        }
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodes.getFkArtworkId());
        if (!ecmArtwork.getFkUserid().equals(ecmArtworkNodeNumberConditionVO.getFkUserId())) {
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        ecmArtworkNodeNumberConditionVO.setUpdataDate(new Date());
        ecmArtworkNodeNumberConditionVO.setNameFlag((byte) INT_ONE);

        return null;
    }

    @Override
    @Transactional
    public ResponseDTO saveArtworkEndings(EcmArtworkEndingsQuery ecmArtworkEndingsQuery) {

        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkEndingsQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        List<EcmArtworkNodes> ecmArtworkNodesVoList = new ArrayList<>();
        List<EcmArtworkEndingsVO> ecmArtworkEndingsVOS = ecmArtworkEndingsQuery.getEcmArtworkEndingsVOS();
        ecmArtworkEndingsVOS.forEach(v -> {
            EcmArtworkNodes ecmArtworkNodesVo = new EcmArtworkNodes();

            if (v.getVideoInfo() != null) {
                v.setVideoCode(v.getVideoInfo().getVideoCode());
                v.setVideoUrl(v.getVideoInfo().getVideoUrl());
                v.setVideoImg(v.getVideoInfo().getNodeImgUrl());
                v.setVideoName(v.getVideoInfo().getVideoName());
                ecmArtworkNodesVo.setVideoUrl(v.getVideoInfo().getVideoUrl());
                ecmArtworkNodesVo.setVideoCode(v.getVideoInfo().getVideoCode());
                ecmArtworkNodesVo.setItemsBakText(v.getVideoInfo().getNodeImgUrl());
            }

            v.setComment(v.getSelectTree().replace("[", "").replace("]", "").replace(",", "").replace(" ", ""));
            v.setFkArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());

            ecmArtworkNodesVo.setVideoText(v.getSelectTitle());
            ecmArtworkNodesVo.setParentId(-1);
            ecmArtworkNodesVo.setFkArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
            ecmArtworkNodesVo.setIsDeleted("N");

            ecmArtworkNodesVoList.add(ecmArtworkNodesVo);

        });

        //??????list

        // ????????????
        try {
            List<EcmArtworkEndingsVO> ecmArtworkEndingsList = ecmArtworkEndingsDao.selectByArtwId(ecmArtworkEndingsQuery.getFkArtworkId());
            if (!CollectionUtils.isEmpty(ecmArtworkEndingsList)) {
                if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOS)) {
                    ecmArtworkNodesDao.deleteEcmArtworkEndingsByArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
                }
                ecmArtworkEndingsDao.deleteByArtwork(ecmArtworkEndingsQuery.getFkArtworkId());
            }
            if (CollectionUtils.isEmpty(ecmArtworkEndingsVOS)) {
                return ResponseDTO.ok();
            }
            ecmArtworkNodesDao.insertEndingList(ecmArtworkNodesVoList);
            for (int i = 0; i < ecmArtworkNodesVoList.size(); i++) {
                ecmArtworkEndingsVOS.get(i).setFkNodeId(ecmArtworkNodesVoList.get(i).getPkDetailId());
            }
            ecmArtworkEndingsDao.insertSelectiveList(ecmArtworkEndingsVOS);
            System.out.println();
            return ResponseDTO.ok();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return ResponseDTO.fail("????????????");
        }

        // ????????????


    }

    @Override
    public ResponseDTO saveArtworkEndingState(EcmArtworkVo ecmArtworkVo) {
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtworkVo.getPkArtworkId());
        if (ecmArtwork == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtwork.getFkUserid())) {
            return ResponseDTO.fail("????????????");
        }
        ecmArtworkDao.updateEndingsByArtwork(ecmArtworkVo);
        return ResponseDTO.ok();
    }

    @Override
    @Transactional
    public ResponseDTO saveArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkEndingsQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkEndingsQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }
        List<EcmArtworkNodes> ecmArtworkNodesVoList = new ArrayList<>();
//        List<EcmArtworkNodePopupSettings> ecmArtworkNodePopupSettingsList = new ArrayList<>();
        List<EcmArtworkEndingsVO> ecmArtworkEndingsVOS = ecmArtworkEndingsQuery.getEcmArtworkEndingsVOS();
        ecmArtworkEndingsVOS.forEach(v -> {
            EcmArtworkNodes ecmArtworkNodesVo = new EcmArtworkNodes();
            if (v.getVideoInfo() != null) {
                v.setVideoCode(v.getVideoInfo().getVideoCode());
                v.setVideoUrl(v.getVideoInfo().getVideoUrl());
                v.setVideoImg(v.getVideoInfo().getNodeImgUrl());
                v.setVideoName(v.getVideoInfo().getVideoName());
                ecmArtworkNodesVo.setVideoUrl(v.getVideoInfo().getVideoUrl());
                ecmArtworkNodesVo.setVideoCode(v.getVideoInfo().getVideoCode());
                ecmArtworkNodesVo.setItemsBakText(v.getVideoInfo().getNodeImgUrl());
            }
            v.setComment(v.getSelectTree().replace("[", "").replace("]", "").replace(",", "").replace(" ", ""));
            v.setFkArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
            ecmArtworkNodesVo.setVideoText(v.getSelectTitle());
            ecmArtworkNodesVo.setParentId(-1);
            ecmArtworkNodesVo.setFkArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
            ecmArtworkNodesVo.setIsDeleted("N");
            ecmArtworkNodesVoList.add(ecmArtworkNodesVo);
        });
        //??????list
        // ????????????
        try {
//            List<EcmArtworkEndingsVO> ecmArtworkEndingsVOList = ecmArtworkEndingsDao.selectByArtwId(ecmArtworkEndingsQuery.getFkArtworkId());
//            if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOList)) {
//                if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOS)) {
//                    ecmArtworkNodesDao.deleteEcmArtworkEndingsByArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
//                }
//                ecmArtworkEndingsDao.deleteByArtwork(ecmArtworkEndingsQuery.getFkArtworkId());
//            }
            if (CollectionUtils.isEmpty(ecmArtworkEndingsVOS)) {
                return ResponseDTO.ok();
            }
            ecmArtworkNodesDao.insertEndingList(ecmArtworkNodesVoList);
            for (int i = 0; i < ecmArtworkNodesVoList.size(); i++) {
                ecmArtworkEndingsVOS.get(i).setFkNodeId(ecmArtworkNodesVoList.get(i).getPkDetailId());
//                EcmArtworkNodePopupSettings ecmArtworkNodePopupSettings =  ecmArtworkEndingsVOS.get(i).getEcmArtworkNodePopupSettings();
//                ecmArtworkNodePopupSettings.setFkNodeId(ecmArtworkNodesVoList.get(i).getPkDetailId());
//                ecmArtworkNodePopupSettingsList.add(ecmArtworkNodePopupSettings);
            }
//            ecmArtworkNodePopupSettingsDao.insertSelectiveList(ecmArtworkNodePopupSettingsList);
            ecmArtworkEndingsDao.insertSelectiveList(ecmArtworkEndingsVOS);
            return ResponseDTO.ok(ecmArtworkEndingsVOS);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return ResponseDTO.fail("????????????");
        }
    }

    @Override
    @Transactional
    public ResponseDTO updateArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkEndingsQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkEndingsQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }
        List<EcmArtworkNodes> ecmArtworkNodesVoList = new ArrayList<>();
        List<EcmArtworkEndingsVO> ecmArtworkEndingsVOS = ecmArtworkEndingsQuery.getEcmArtworkEndingsVOS();
        ecmArtworkEndingsVOS.forEach(v -> {
            EcmArtworkNodes ecmArtworkNodesVo = new EcmArtworkNodes();
            if (v.getVideoInfo() != null) {
                v.setVideoCode(v.getVideoInfo().getVideoCode());
                v.setVideoUrl(v.getVideoInfo().getVideoUrl());
                v.setVideoImg(v.getVideoInfo().getNodeImgUrl());
                v.setVideoName(v.getVideoInfo().getVideoName());
                ecmArtworkNodesVo.setVideoUrl(v.getVideoInfo().getVideoUrl());
                ecmArtworkNodesVo.setVideoCode(v.getVideoInfo().getVideoCode());
                ecmArtworkNodesVo.setItemsBakText(v.getVideoInfo().getNodeImgUrl());
                ecmArtworkNodesVo.setVideoInfo(v.getVideoInfo().getVideoHigh() + "," + v.getVideoInfo().getVideoWidth() + "," + v.getVideoInfo().getVideoTime());
            }
            v.setComment(v.getSelectTree().replace("[", "").replace("]", "").replace(",", "").replace(" ", ""));
            v.setFkArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
            ecmArtworkNodesVo.setVideoText(v.getSelectTitle());
            ecmArtworkNodesVo.setParentId(-1);
            ecmArtworkNodesVo.setFkArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
            ecmArtworkNodesVo.setIsDeleted("N");
            ecmArtworkNodesVoList.add(ecmArtworkNodesVo);
        });
        //??????list
        // ????????????
        try {
//            List<EcmArtworkEndingsVO> ecmArtworkEndingsVOList = ecmArtworkEndingsDao.selectByArtwId(ecmArtworkEndingsQuery.getFkArtworkId());
//            if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOList)) {
//                if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOS)) {
//                    ecmArtworkNodesDao.deleteEcmArtworkEndingsByArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
//                }
//                ecmArtworkEndingsDao.deleteByArtwork(ecmArtworkEndingsQuery.getFkArtworkId());
//            }

            if (CollectionUtils.isEmpty(ecmArtworkEndingsVOS)) {
                return ResponseDTO.ok();
            }
            // ????????? ???????????????????????????
            List<EcmArtworkEndingsVO> list = ecmArtworkEndingsDao.selectByEndingList(ecmArtworkEndingsVOS);
            for (int i = 0; i < list.size(); i++) {
                ecmArtworkNodesVoList.get(i).setPkDetailId(list.get(i).getFkNodeId());
            }
            ecmArtworkNodesDao.updateSelectiveEndingList(ecmArtworkNodesVoList);

            ecmArtworkEndingsDao.updateSelectiveList(ecmArtworkEndingsVOS);
            return ResponseDTO.ok();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return ResponseDTO.fail("????????????");
        }
    }

    @Override
    @Transactional
    public ResponseDTO deleteArtworkEnding(EcmArtworkEndingsQuery ecmArtworkEndingsQuery) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkEndingsQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkEndingsQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }
        try {
            EcmArtworkEndings ecmArtworkEndings = ecmArtworkEndingsDao.selectByPrimaryKey(ecmArtworkEndingsQuery.getPkEndingsId());
            ecmArtworkNodesDao.deleteByNodeId(ecmArtworkEndings.getFkNodeId());
            ecmArtworkEndingsDao.deleteByPrimaryKey(ecmArtworkEndingsQuery.getPkEndingsId());
            return ResponseDTO.ok();

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseDTO.fail("????????????");
        }

    }

    @Override
    public ResponseDTO getArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery) {
        List<EcmArtworkEndingsVO> ecmArtworkEndingsVOList = ecmArtworkEndingsDao.selectEcmArtworkEndingsQuery(ecmArtworkEndingsQuery);
        List<EcmArtworkNodePopupSettingsVO> ecmArtworkNodePopupSettingsVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOList)) {
            ecmArtworkNodePopupSettingsVOList = ecmArtworkNodePopupSettingsDao.selectByEndingList(ecmArtworkEndingsVOList);

        }
//        Integer count = ecmArtworkEndingsDao.selectCountEcmArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(2);
        if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOList)) {
            for (EcmArtworkEndingsVO ecmArtworkEndingsVO : ecmArtworkEndingsVOList) {
                ecmArtworkEndingsVO.setSelectTreeList(JSON.parseArray(ecmArtworkEndingsVO.getSelectTree(), Integer.class));
                EcmVideoTemporaryStorageVO ecmVideoTemporaryStorageVO = new EcmVideoTemporaryStorageVO();
                ecmVideoTemporaryStorageVO.setVideoCode(ecmArtworkEndingsVO.getVideoCode());
                ecmVideoTemporaryStorageVO.setVideoUrl(ecmArtworkEndingsVO.getVideoUrl());
                ecmVideoTemporaryStorageVO.setNodeImgUrl(ecmArtworkEndingsVO.getVideoImg());
                ecmVideoTemporaryStorageVO.setFkNodeId(ecmArtworkEndingsVO.getFkNodeId());
                ecmArtworkEndingsVO.setVideoInfo(ecmVideoTemporaryStorageVO);

                if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOList)) {
                    ecmArtworkNodePopupSettingsVOList.forEach(ecmArtworkNodePopupSettingsVO -> {
                        if (ecmArtworkEndingsVO.getFkNodeId().equals(ecmArtworkNodePopupSettingsVO.getFkNodeId())) {
                            ecmArtworkNodePopupSettingsVO.setPopupState(ecmArtworkEndingsVO.getPopupState());
                            ecmArtworkEndingsVO.setEcmArtworkNodePopupSettings(ecmArtworkNodePopupSettingsVO);
                        }
                    });
                }
            }
        }
        stringObjectHashMap.put("list", ecmArtworkEndingsVOList);
//        stringObjectHashMap.put("count",count);
        return ResponseDTO.ok(ecmArtworkEndingsVOList);
    }


    @Override
    @Transactional
    public ResponseDTO saveArtworkEndingAll(EcmArtworkEndingsQuery ecmArtworkEndingsQuery) {

        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkEndingsQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkEndingsQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }

        List<String> endingAll = EndingUntil.getEndingAll(ecmArtworkEndingsQuery.getNodeNum());
//        CollectionUtils.
        Collections.reverse(endingAll);
        List<EcmArtworkNodes> ecmArtworkNodesVoList = new ArrayList<>();
        List<EcmArtworkEndingsVO> ecmArtworkEndingsVOS = new ArrayList<>();
        endingAll.forEach(v -> {
            EcmArtworkEndingsVO ecmArtworkEndingsVO = new EcmArtworkEndingsVO();
            ecmArtworkEndingsVO.setFkArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
            ecmArtworkEndingsVO.setSelectTree(v);
            ecmArtworkEndingsVO.setSelectTitle("????????????");
            ecmArtworkEndingsVO.setComment(v.replace("[", "").replace("]", "").replace(",", "").replace(" ", ""));
//            ecmArtworkEndingsVO.set


            EcmArtworkNodes ecmArtworkNodesVo = new EcmArtworkNodes();
            ecmArtworkNodesVo.setVideoText("????????????");
            ecmArtworkNodesVo.setParentId(-1);
            ecmArtworkNodesVo.setFkArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
            ecmArtworkNodesVo.setIsDeleted("N");
            ecmArtworkEndingsVOS.add(ecmArtworkEndingsVO);
            ecmArtworkNodesVoList.add(ecmArtworkNodesVo);
        });
        //??????list
        // ????????????
        try {
            List<EcmArtworkEndingsVO> ecmArtworkEndingsVOList = ecmArtworkEndingsDao.selectByArtwId(ecmArtworkEndingsQuery.getFkArtworkId());
            if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOList)) {
                if (!CollectionUtils.isEmpty(ecmArtworkEndingsVOS)) {
                    ecmArtworkNodesDao.deleteEcmArtworkEndingsByArtworkId(ecmArtworkEndingsQuery.getFkArtworkId());
                }
                ecmArtworkEndingsDao.deleteByArtwork(ecmArtworkEndingsQuery.getFkArtworkId());
            }
            if (CollectionUtils.isEmpty(ecmArtworkEndingsVOS)) {
                return ResponseDTO.ok();
            }
            ecmArtworkNodesDao.insertEndingList(ecmArtworkNodesVoList);
            for (int i = 0; i < ecmArtworkNodesVoList.size(); i++) {
                ecmArtworkEndingsVOS.get(i).setFkNodeId(ecmArtworkNodesVoList.get(i).getPkDetailId());
            }
            ecmArtworkEndingsDao.insertSelectiveList(ecmArtworkEndingsVOS);
            return ResponseDTO.ok(ecmArtworkEndingsVOS);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return ResponseDTO.fail("????????????");
        }

    }

    @Override
    @Transactional
    public ResponseDTO deleteArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkEndingsQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkEndingsQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }
        try {
            List<EcmArtworkEndingsVO> ecmArtworkEndings = ecmArtworkEndingsDao.selectByPrimaryKeyList(ecmArtworkEndingsQuery.getDeleteEndingId());
            ecmArtworkNodesDao.deleteByPrimaryKeyList(ecmArtworkEndings);
            ecmArtworkEndingsDao.deleteByPrimaryList(ecmArtworkEndings);
            return ResponseDTO.ok();

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseDTO.fail("????????????");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO saveArtworkNodePopupSettings(EcmArtworkNodePopupSettingsVO ecmArtworkNodePopupSettingsVO) {

        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodePopupSettingsVO.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }

        EcmArtworkNodes ecmArtworkNodes = ecmArtworkNodesDao.selectByPrimaryKey(ecmArtworkNodePopupSettingsVO.getFkNodeId());
        if (ecmArtworkNodes == null || !ecmArtworkNodes.getFkArtworkId().equals(ecmArtworkVo.getPkArtworkId())) {
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        ecmArtworkNodes.setPopupState(ecmArtworkNodePopupSettingsVO.getPopupState());
        EcmArtworkNodePopupSettingsVO artworkNodePopupSettingsVO = ecmArtworkNodePopupSettingsDao.selectByArtworkNodeId(ecmArtworkNodePopupSettingsVO.getFkNodeId());
        try {
            ecmArtworkNodesDao.updatePopupSetting(ecmArtworkNodes);
            if (ecmArtworkNodePopupSettingsVO.getEndingPopup() == 1) {
                EcmArtworkEndingsVO ecmArtworkEndingsVO = ecmArtworkEndingsDao.selectByNodeId(ecmArtworkNodePopupSettingsVO.getFkNodeId());
                if (ecmArtworkEndingsVO != null) {
                    ecmArtworkEndingsVO.setPopupState(ecmArtworkNodePopupSettingsVO.getPopupState());
                    ecmArtworkEndingsDao.updatePopupStateByPrimaryKey(ecmArtworkEndingsVO);
                }
            }
            if (artworkNodePopupSettingsVO == null) {
                ecmArtworkNodePopupSettingsVO.setCreateTime(new Date());
                ecmArtworkNodePopupSettingsDao.insertSelective(ecmArtworkNodePopupSettingsVO);
                ecmArtworkNodePopupSettingsDao.updateNameByArtWorkSelective(ecmArtworkNodePopupSettingsVO);
            } else {
                ecmArtworkNodePopupSettingsVO.setUpdateTime(new Date());
                ecmArtworkNodePopupSettingsVO.setPkNodePopupSettingsId(artworkNodePopupSettingsVO.getPkNodePopupSettingsId());
                ecmArtworkNodePopupSettingsDao.updateByPrimaryKeySelective(ecmArtworkNodePopupSettingsVO);
                if (!artworkNodePopupSettingsVO.getPopupName().equals(ecmArtworkNodePopupSettingsVO.getPopupName()) || !artworkNodePopupSettingsVO.getPopupNameState().equals(ecmArtworkNodePopupSettingsVO.getPopupNameState())) {
                    ecmArtworkNodePopupSettingsDao.updateNameByArtWorkSelective(ecmArtworkNodePopupSettingsVO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseDTO.ok(ecmArtworkNodePopupSettingsVO);
    }

    @Override
    public ResponseDTO saveArtworkNodeCondition(EcmArtworkNodesVo ecmArtworkNodesVo) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodesVo.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        EcmArtworkNodes ecmArtworkNodes = ecmArtworkNodesDao.selectByPrimaryKey(ecmArtworkNodesVo.getPkDetailId());
        if (ecmArtworkNodes == null) {
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodesVo);
        return ResponseDTO.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO saveArtworkEndingCondition(EcmArtworkNodesVo ecmArtworkNodesVo) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodesVo.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        EcmArtworkNodes ecmArtworkNodes = ecmArtworkNodesDao.selectByPrimaryKey(ecmArtworkNodesVo.getPkDetailId());
        if (ecmArtworkNodes == null) {
            return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
        }
        EcmArtworkEndings ecmArtworkEndings = new EcmArtworkEndings();
        ecmArtworkEndings.setFkArtworkId(ecmArtworkNodesVo.getFkArtworkId());
        ecmArtworkEndings.setConditionState(ecmArtworkNodesVo.getConditionState());
        try {
            ecmArtworkNodesDao.updateEndingConditionByArtworkId(ecmArtworkNodes);
            ecmArtworkEndingsDao.updateConditionByArtworkId(ecmArtworkEndings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseDTO.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO saveArtworkNodeBuoy(EcmArtworkNodeBuoyQuery ecmArtworkNodeBuoyQuery) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodeBuoyQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkNodeBuoyQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }

        List<EcmArtworkNodeBuoy> ecmArtworkNodeBuoyList = ecmArtworkNodeBuoyQuery.getEcmArtworkNodeBuoyList();
        EcmArtworkNodes ecmArtworkNodes = ecmArtworkNodesDao.selectByPrimaryKey(ecmArtworkNodeBuoyList.get(0).getFkNodeId());
        List<EcmArtworkNodeBuoyVO> ecmArtworkNodeBuoyVOList = ecmArtworkNodeBuoyDao.selectByEcmArtworkId(ecmArtworkNodeBuoyQuery.getFkArtworkId());

        List<EcmArtworkNodeBuoy> upDataEcmArtworkNodeBuoyVOList = new ArrayList<>();

        Iterator<EcmArtworkNodeBuoy> iterator = ecmArtworkNodeBuoyList.iterator();
        while (iterator.hasNext()) {
            EcmArtworkNodeBuoy next = iterator.next();
//            next.setBuoyStatus("0");
            if (next.getBuoyType().equals(0)) {
                next.setBuoyPlayEndType(ecmArtworkNodeBuoyQuery.getBuoyPlayEndType());
            }

            for (EcmArtworkNodeBuoy ecmArtworkNodeBuoy : ecmArtworkNodeBuoyVOList) {
                ecmArtworkNodeBuoy.setFkArtworkId(ecmArtworkNodeBuoyQuery.getFkArtworkId());
                if (ecmArtworkNodeBuoy.getPkBuoyId().equals(next.getPkBuoyId())) {
//                    ecmArtworkNodeBuoy =  next;
                    upDataEcmArtworkNodeBuoyVOList.add(next);
                    iterator.remove();
                }
            }
        }
        try {
            if (!CollectionUtils.isEmpty(ecmArtworkNodeBuoyList)) {
                // ??????
                ecmArtworkNodeBuoyDao.insertSelectiveList(ecmArtworkNodeBuoyList);
            }
            if (!CollectionUtils.isEmpty(upDataEcmArtworkNodeBuoyVOList)) {
                // ??????
                ecmArtworkNodeBuoyDao.updateByPrimaryKeySelectiveList(upDataEcmArtworkNodeBuoyVOList);
            }
            ecmArtworkNodesDao.updateArtworkNodeBuoyByFkNodeId(ecmArtworkNodes.getParentId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.fail("10086");
        }
        // ???????????? ????????????????????????????????????
        upDataEcmArtworkNodeBuoyVOList.addAll(ecmArtworkNodeBuoyList);
        return ResponseDTO.ok(upDataEcmArtworkNodeBuoyVOList);
    }

    @Override
    public ResponseDTO deleteArtworkNodeBuoy(EcmArtworkNodeBuoyVO ecmArtworkNodeBuoyVO) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodeBuoyVO.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkNodeBuoyVO.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }
        if (ecmArtworkNodeBuoyVO.getPkBuoyId() != null) {
            ecmArtworkNodeBuoyDao.deleteByPrimaryKey(ecmArtworkNodeBuoyVO.getPkBuoyId());
            return ResponseDTO.ok();
        }

        return ResponseDTO.fail("???????????????");

    }

    @Override
    public ResponseDTO getArtworkNodeBuoy(EcmArtworkNodeBuoyQuery ecmArtworkNodeBuoyQuery) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodeBuoyQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkNodeBuoyQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }
        if (CollectionUtils.isEmpty(ecmArtworkNodeBuoyQuery.getFkNodeIdList())) {
            return ResponseDTO.fail("????????????");
        }
        List<EcmArtworkNodeBuoyVO> ecmArtworkNodeBuoyVOList = ecmArtworkNodeBuoyDao.selectByEcmNodeIdList(ecmArtworkNodeBuoyQuery.getFkNodeIdList());
        ecmArtworkNodeBuoyVOList.forEach(v -> v.setComparingTime(Integer.valueOf(v.getBuoySectionTime())));
        // ??????
        List<EcmArtworkNodeBuoyVO> sortEcmArtworkNodeBuoyVOList = ecmArtworkNodeBuoyVOList.stream().sorted(Comparator.comparing(EcmArtworkNodeBuoyVO::getBuoyType).thenComparing(EcmArtworkNodeBuoyVO::getComparingTime)).collect(Collectors.toList());
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(2);
        stringObjectHashMap.put("list",sortEcmArtworkNodeBuoyVOList);
        if (!CollectionUtils.isEmpty(sortEcmArtworkNodeBuoyVOList)) {
            stringObjectHashMap.put("buoyPlayEndType",sortEcmArtworkNodeBuoyVOList.get(0).getBuoyPlayEndType());
        }else {
            stringObjectHashMap.put("buoyPlayEndType",-1);
        }

        return ResponseDTO.ok(stringObjectHashMap);
    }

    @Override
    public ResponseDTO updateArtworkNodeBuoy(EcmArtworkNodesVo ecmArtworkNodesVo) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodesVo.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (ecmArtworkNodesVo.getFkUserId().equals(ecmArtworkVo.getFkUserid())) {
            return ResponseDTO.fail("????????????");
        }
        return ResponseDTO.get(ecmArtworkNodesDao.updateArtworkNodeBuoy(ecmArtworkNodesVo) == 1);
    }

    @Override
    public ResponseDTO migrateArtworkNode(EcmArtworkNodesVo ecmArtworkNodesVo) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodesVo.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }


        List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(ecmArtworkNodesVo.getFkArtworkId());
        if (list.isEmpty()) {
            return ResponseDTO.fail("??????id????????????");
        }
        // ????????????????????????
//        List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNode -> !JudgeConstant.Y.equals(ecmArtworkNode.getIsDeleted())).collect(Collectors.toList());

        EcmArtworkNodesVo ecmArtworkNodesTree = TreeUtil.buildTreeByDetailId(list, ecmArtworkNodesVo.getNewParentsId()).get(INT_ZORE);

        EcmArtworkNodesVo pArtworkNodesTree = TreeUtil.buildTreeByDetailId(list, ecmArtworkNodesVo.getPkDetailId()).get(INT_ZORE);

        EcmArtworkNodes ecmArtworkNodes = ecmArtworkNodesDao.selectByPrimaryKey(ecmArtworkNodesVo.getPkDetailId());
        List<EcmArtworkNodes> ecmArtworkNodesList = new ArrayList<>();

        String pXId = ecmArtworkNodesTree.getRevolutionId();


        if (!CollectionUtils.isEmpty(ecmArtworkNodesTree.getNodesVos())){
            String cXId = ecmArtworkNodesTree.getNodesVos().get(ecmArtworkNodesTree.getNodesVos().size() - 1).getRevolutionId();

            String cXIdBase = cXId.substring(0, pXId.length() ) + (Integer.parseInt(cXId.substring(cXId.length() - 1)) + 1);

            ecmArtworkNodesVo.setRevolutionId(cXIdBase);
            ecmArtworkNodesVo.setParentId(ecmArtworkNodesVo.getNewParentsId());
            ecmArtworkNodesList.add(ecmArtworkNodesVo);

            replaceString(pArtworkNodesTree.getNodesVos(),ecmArtworkNodesList, ecmArtworkNodes.getRevolutionId(), cXIdBase);

        }else {

            String cXIdBase = pXId + "1";
            ecmArtworkNodesVo.setRevolutionId(cXIdBase);
            ecmArtworkNodesVo.setParentId(ecmArtworkNodesVo.getNewParentsId());
            ecmArtworkNodesList.add(ecmArtworkNodesVo);
            replaceString(pArtworkNodesTree.getNodesVos(),ecmArtworkNodesList, ecmArtworkNodes.getRevolutionId(),cXIdBase);

        }
        ecmArtworkNodesDao.updateMigrateByEcmArtworkNodesList(ecmArtworkNodesList);
        return ResponseDTO.ok();

    }

    @Override
    public ResponseDTO saveArtworkNodeAction(EcmArtworkNodeActionQuery ecmArtworkNodeActionQuery) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodeActionQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkNodeActionQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }
        List<EcmArtworkNodeAction> ecmArtworkNodeActionList = ecmArtworkNodeActionQuery.getEcmArtworkNodeActionList();
        List<EcmArtworkNodeActionVO> ecmArtworkNodeActionVOS = ecmArtworkNodeActionDao.selectByFkArtworkId(ecmArtworkNodeActionQuery.getFkArtworkId());
        List<EcmArtworkNodeAction> upDataArtworkNodeActions = new ArrayList<>();

        Iterator<EcmArtworkNodeAction> iterator = ecmArtworkNodeActionList.iterator();

        while (iterator.hasNext()) {
            EcmArtworkNodeAction next = iterator.next();
            for (EcmArtworkNodeActionVO ecmArtworkNodeActionVO : ecmArtworkNodeActionVOS) {
                if (ecmArtworkNodeActionVO.getPkActionId().equals(next.getPkActionId())){
                    upDataArtworkNodeActions.add(next);
                    iterator.remove();
                }

            }
        }

        try {
            if (!CollectionUtils.isEmpty(ecmArtworkNodeActionList)){
                // ??????
                ecmArtworkNodeActionDao.insertSelectiveList(ecmArtworkNodeActionList);
            }
            if (!CollectionUtils.isEmpty(upDataArtworkNodeActions)) {
                // ??????
                ecmArtworkNodeActionDao.updateByPrimaryKeySelectiveList(upDataArtworkNodeActions);
            }

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.fail("10086");
        }
        // ???????????? ????????????????????????????????????
        upDataArtworkNodeActions.addAll(ecmArtworkNodeActionList);
        return ResponseDTO.ok(upDataArtworkNodeActions);

    }

    @Override
    public ResponseDTO getArtworkNodeAction(EcmArtworkNodeActionQuery ecmArtworkNodeActionQuery) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodeActionQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkNodeActionQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }
        if (CollectionUtils.isEmpty(ecmArtworkNodeActionQuery.getFkNodeIdList())) {
            return ResponseDTO.fail("????????????");
        }
        List<EcmArtworkNodeActionVO> ecmArtworkNodeActionVOS = ecmArtworkNodeActionDao.selectByEcmNodeIdList(ecmArtworkNodeActionQuery.getFkNodeIdList());
        return ResponseDTO.ok(ecmArtworkNodeActionVOS);
    }


    @Override
    public ResponseDTO saveArtworkNodePanoramicBuoy(EcmArtworkNodeBuoyPanoramicQuery ecmArtworkNodeBuoyPanoramicQuery) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodeBuoyPanoramicQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkNodeBuoyPanoramicQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }

        List<EcmArtworkNodeBuoyPanoramic> ecmArtworkNodeBuoyList = ecmArtworkNodeBuoyPanoramicQuery.getEcmArtworkNodeBuoyList();
        EcmArtworkNodes ecmArtworkNodes = ecmArtworkNodesDao.selectByPrimaryKey(ecmArtworkNodeBuoyList.get(0).getFkNodeId());
        List<EcmArtworkNodeBuoyPanoramicVO> ecmArtworkNodeBuoyVOList = ecmArtworkNodeBuoyPanoramicDao.selectByEcmArtworkId(ecmArtworkNodeBuoyPanoramicQuery.getFkArtworkId());

        List<EcmArtworkNodeBuoyPanoramic> upDataEcmArtworkNodeBuoyVOList = new ArrayList<>();

        Iterator<EcmArtworkNodeBuoyPanoramic> iterator = ecmArtworkNodeBuoyList.iterator();
        while (iterator.hasNext()) {
            EcmArtworkNodeBuoyPanoramic next = iterator.next();
//            next.setBuoyStatus("0");
              next.setBuoyPlayEndType(ecmArtworkNodeBuoyPanoramicQuery.getBuoyPlayEndType());


            for (EcmArtworkNodeBuoyPanoramic ecmArtworkNodeBuoy : ecmArtworkNodeBuoyVOList) {
                ecmArtworkNodeBuoy.setFkArtworkId(ecmArtworkNodeBuoyPanoramicQuery.getFkArtworkId());
                if (ecmArtworkNodeBuoy.getPkBuoyId().equals(next.getPkBuoyId())) {
//                    ecmArtworkNodeBuoy =  next;
                    upDataEcmArtworkNodeBuoyVOList.add(next);
                    iterator.remove();
                }
            }
        }
        try {
            if (!CollectionUtils.isEmpty(ecmArtworkNodeBuoyList)) {
                // ??????
                ecmArtworkNodeBuoyPanoramicDao.insertSelectiveList(ecmArtworkNodeBuoyList);
            }
            if (!CollectionUtils.isEmpty(upDataEcmArtworkNodeBuoyVOList)) {
                // ??????
                ecmArtworkNodeBuoyPanoramicDao.updateByPrimaryKeySelectiveList(upDataEcmArtworkNodeBuoyVOList);
            }
            ecmArtworkNodesDao.updateArtworkNodeBuoyByFkNodeId(ecmArtworkNodes.getParentId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.fail("10086");
        }
        // ???????????? ????????????????????????????????????
        upDataEcmArtworkNodeBuoyVOList.addAll(ecmArtworkNodeBuoyList);
        return ResponseDTO.ok(upDataEcmArtworkNodeBuoyVOList);
    }

    @Override
    public ResponseDTO getArtworkNodePanoramicBuoy(EcmArtworkNodeBuoyPanoramicQuery ecmArtworkNodeBuoyPanoramicQuery) {
        EcmArtwork ecmArtworkVo = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodeBuoyPanoramicQuery.getFkArtworkId());
        if (ecmArtworkVo == null) {
            return ResponseDTO.fail("????????????");
        }
        if (!ecmArtworkVo.getFkUserid().equals(ecmArtworkNodeBuoyPanoramicQuery.getFkUserId())) {
            return ResponseDTO.fail("????????????");
        }
        if (CollectionUtils.isEmpty(ecmArtworkNodeBuoyPanoramicQuery.getFkNodeIdList())) {
            return ResponseDTO.fail("????????????");
        }
        List<EcmArtworkNodeBuoyPanoramicVO> ecmArtworkNodeBuoyVOList = ecmArtworkNodeBuoyPanoramicDao.selectByEcmNodeIdList(ecmArtworkNodeBuoyPanoramicQuery.getFkNodeIdList());
//        ecmArtworkNodeBuoyVOList.forEach(v -> v.setComparingTime(Integer.valueOf(v.getBuoySectionTime())));
        // ??????
//        List<EcmArtworkNodeBuoyVO> sortEcmArtworkNodeBuoyVOList = ecmArtworkNodeBuoyVOList.stream().sorted(Comparator.comparing(EcmArtworkNodeBuoyVO::getBuoyType).thenComparing(EcmArtworkNodeBuoyVO::getComparingTime)).collect(Collectors.toList());
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(2);
        stringObjectHashMap.put("list",ecmArtworkNodeBuoyVOList);
        if (!CollectionUtils.isEmpty(ecmArtworkNodeBuoyVOList)) {
            stringObjectHashMap.put("buoyPlayEndType",ecmArtworkNodeBuoyVOList.get(0).getBuoyPlayEndType());
        }else {
            stringObjectHashMap.put("buoyPlayEndType",-1);
        }

        return ResponseDTO.ok(stringObjectHashMap);
    }


    /**
     * @param: [ecmArtworkNodesTree ?????????????????????????????? ???????????????childs ????????????, ecmArtworkNodes ?????????????????? ???????????????????????? , rXIdBase ???????????? xid, cXIdBase ????????? xid]
     * @return: void
     * @author: cxd
     * @Date: 2021/4/19
     * ?????? :  ?????????????????? ?????? ???????????? xid ????????????
     */
    private void replaceString ( List<EcmArtworkNodesVo> ecmArtworkNodesTree,  List<EcmArtworkNodes> ecmArtworkNodes,String rXIdBase , String cXIdBase) {

        if (!CollectionUtils.isEmpty(ecmArtworkNodesTree)){
            ecmArtworkNodesTree.forEach( v -> {
                v.setRevolutionId(v.getRevolutionId().replaceAll(rXIdBase,cXIdBase));
                ecmArtworkNodes.add(v);
                replaceString(v.getNodesVos(),ecmArtworkNodes, rXIdBase,cXIdBase);
            });
        }


    }



    /**
     *
     * ???????????????
     *
     */

    @Override
    public ResponseDTO getFindArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {
        // ?????????????????? ????????????
        List<EcmArtworkBroadcastHotVO> ecmArtworkBroadcastHotVOS = ecmArtworkBroadcastHotDao.selectFindAll(ecmArtWorkQuery);
        if (CollectionUtils.isEmpty(ecmArtworkBroadcastHotVOS)) {
            return ResponseDTO.fail(ErrorEnum.ERR_601.getText());
        }
        // ??????????????? ???????????? ???????????????
        List<EcmArtworkVo> list = ecmArtworkDao.selectFindArtWorks(ecmArtworkBroadcastHotVOS);
        List<EcmUserVo> userVoList = ecmUserDao.selectUserByEcmArtworkList(list);
        // ??????????????????
        list.forEach(ecmArtworkVo -> {
            userVoList.forEach(ecmUserVo -> {
                if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())) {
                    ecmArtworkVo.setUserName(ecmUserVo.getUsername());
                    if (!StringUtils.isEmpty(ecmUserVo.getUserLogoUrl())) {
                        ecmArtworkVo.setUserLogoUrl(ecmUserVo.getUserLogoUrl());
                    }
                }
            });
            ecmArtworkBroadcastHotVOS.forEach(ecmArtworkBroadcastHotVO -> {
                if (ecmArtworkBroadcastHotVO.getFkArkworkId().equals(ecmArtworkVo.getPkArtworkId())) {
                    ecmArtworkVo.setHotCount(ecmArtworkBroadcastHotVO.getBroadcastCount());
                }
            });
        });
//        EcmArtworkVo ecmArtworkVo = new EcmArtworkVo();
//        ecmArtworkVo.setUserName("ad");
//        ecmArtworkVo.setLogoPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599039415911&di=41f622b164548552e3e407e5e955b940&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F52%2F52%2F01200000169026136208529565374.jpg");
//        ecmArtworkVo.setArtworkName("?????????XXX???");
//        ecmArtworkVo.setCode("ad");
//        list.add(1, ecmArtworkVo);
        Map<String, Object> map = new HashMap<>(2);
        if (ecmArtworkBroadcastHotVOS.size() < ecmArtWorkQuery.getLimit()) {
            map.put("loadStatus", 1);
        }
        map.put("list", list);
        return ResponseDTO.ok("sucess", map);
    }


    @Override
    public ResponseDTO getFindSortArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {
        // ??????????????? ??????
        List<EcmArtworkVo> list = ecmArtworkDao.selectFindSortArtWorks(ecmArtWorkQuery);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseDTO.fail(ErrorEnum.ERR_601.getText());
        }
        List<EcmUserVo> userVoList = ecmUserDao.selectUserByEcmArtworkList(list);
        // ??????????????? ?????? ??? ??????
        List<EcmArtworkBroadcastHotVO> ecmArtworkBroadcastHotVOS = ecmArtworkBroadcastHotDao.selectEcmArtworkList(list);
        // ??????????????? ????????????
        list.forEach(ecmArtworkVo -> {
            userVoList.forEach(ecmUserVo -> {
                // ??????????????????
                if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())) {
                    ecmArtworkVo.setUserName(ecmUserVo.getUsername());
                }
                // ??????????????????
                if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())) {
                    ecmArtworkVo.setUserName(ecmUserVo.getUsername());
                    if (!StringUtils.isEmpty(ecmUserVo.getUserLogoUrl())) {
                        ecmArtworkVo.setUserLogoUrl(ecmUserVo.getUserLogoUrl());
                    }
                }

                ecmArtworkBroadcastHotVOS.forEach(ecmArtworkBroadcastHotVO -> {
                    if (ecmArtworkBroadcastHotVO.getFkArkworkId().equals(ecmArtworkVo.getPkArtworkId())) {
                        ecmArtworkVo.setHotCount(ecmArtworkBroadcastHotVO.getBroadcastCount());
                    }
                });
                // ????????????????????? ?????? ?????? ??????
                if (ecmArtworkVo.getHotCount() == null) {
                    ecmArtworkVo.setHotCount(RandomUtil.getCode(3));
                }

            });
        });
        // ??????????????????????????????

        //????????????
//        EcmArtworkVo ecmArtworkVo = new EcmArtworkVo();
//        ecmArtworkVo.setUserName("ad");
//        ecmArtworkVo.setLogoPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599039415911&di=41f622b164548552e3e407e5e955b940&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F52%2F52%2F01200000169026136208529565374.jpg");
//        ecmArtworkVo.setArtworkName("?????????XXX???");
//        ecmArtworkVo.setCode("ad");
//        list.add(1, ecmArtworkVo);
        return ResponseDTO.ok("sucess", list);
    }


    @Override
    public ResponseDTO getRankingArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {
        List<EcmArtworkBroadcastHotVO> ecmArtworkBroadcastHotVOS = ecmArtworkBroadcastHotDao.selectFindAll(ecmArtWorkQuery);
        if (CollectionUtils.isEmpty(ecmArtworkBroadcastHotVOS)) {
            return ResponseDTO.fail(ErrorEnum.ERR_601.getText());
        }
        List<EcmArtworkVo> list = ecmArtworkDao.selectFindArtWorks(ecmArtworkBroadcastHotVOS);
        List<EcmUserVo> userVoList = ecmUserDao.selectUserByEcmArtworkList(list);
        list.forEach(ecmArtworkVo -> {
            userVoList.forEach(ecmUserVo -> {
                if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())) {
                    ecmArtworkVo.setUserName(ecmUserVo.getUsername());
                    if (!StringUtils.isEmpty(ecmUserVo.getUserLogoUrl())) {
                        ecmArtworkVo.setUserLogoUrl(ecmUserVo.getUserLogoUrl());
                    }
                }
            });
            ecmArtworkBroadcastHotVOS.forEach(ecmArtworkBroadcastHotVO -> {
                if (ecmArtworkBroadcastHotVO.getFkArkworkId().equals(ecmArtworkVo.getPkArtworkId())) {
                    ecmArtworkVo.setHotCount(ecmArtworkBroadcastHotVO.getBroadcastCount());
                }
            });
        });

        return ResponseDTO.ok("success", list);
    }

    @Override
    public ResponseDTO search(EcmArtWorkQuery ecmArtWorkQuery) {
        List<EcmArtworkVo> list = ecmArtworkDao.selectSearchArtworkName(ecmArtWorkQuery);

        List<EcmUserVo> userVoList = ecmUserDao.selectUserByEcmArtworkList(list);

        List<EcmArtworkBroadcastHotVO> ecmArtworkBroadcastHotVOS = ecmArtworkBroadcastHotDao.selectEcmArtworkList(list);
        list.forEach(ecmArtworkVo -> {
            userVoList.forEach(ecmUserVo -> {
                // ??????????????????
                if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())) {
                    ecmArtworkVo.setUserName(ecmUserVo.getUsername());
                }
                // ??????????????????
                if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())) {
                    ecmArtworkVo.setUserName(ecmUserVo.getUsername());
                    if (!StringUtils.isEmpty(ecmUserVo.getUserLogoUrl())) {
                        ecmArtworkVo.setUserLogoUrl(ecmUserVo.getUserLogoUrl());
                    }
                }
                //????????????
                ecmArtworkBroadcastHotVOS.forEach(ecmArtworkBroadcastHotVO -> {
                    if (ecmArtworkBroadcastHotVO.getFkArkworkId().equals(ecmArtworkVo.getPkArtworkId())) {
                        ecmArtworkVo.setHotCount(ecmArtworkBroadcastHotVO.getBroadcastCount());
                    }
                });
                // ????????????????????? ?????? ?????? ??????
                if (ecmArtworkVo.getHotCount() == null) {
                    ecmArtworkVo.setHotCount(RandomUtil.getCode(3));
                }

            });
        });
        return ResponseDTO.ok("success", list);
    }

    @Override
    public ResponseDTO getArtWorkNodes(EcmArtWorkQuery ecmArtWorkQuery) {

        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtWorkQuery.getPkArtworkId());
        if (ecmArtwork == null) {
            return ResponseDTO.fail(ErrorEnum.ERR_003.getText());
        }

        List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(ecmArtWorkQuery.getPkArtworkId());
        //?????? ??????
        List<EcmArtworkNodesVo> lists = list.stream().filter(ecmArtworkNodesVo -> !JudgeConstant.Y.equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());
        Map<Integer, List<EcmArtworkNodesVo>> collect = lists.stream().filter(ecmArtworkNodesVo -> !JudgeConstant.Y.equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.groupingBy(EcmArtworkNodes::getParentId));

        List<EcmArtworkNodesDTO> artworkNodesDTOS = new ArrayList<>();
        lists.forEach(node -> {
            //???????????????????????????????????????
            EcmArtworkNodesDTO ecmArtworkNodesDTO = new EcmArtworkNodesDTO();
            BeanUtils.copyProperties(node, ecmArtworkNodesDTO);
            ecmArtworkNodesDTO.setBrotherNode(collect.get(node.getParentId()));
            artworkNodesDTOS.add(ecmArtworkNodesDTO);
        });
        // ?????? ??????????????????
        artworkNodesDTOS.forEach(v -> {
            for (EcmArtworkNodesDTO ecmArtworkNodesDTO : artworkNodesDTOS) {
                if (!StringUtils.isEmpty(ecmArtworkNodesDTO.getItems())) {
                    if (v.getPkDetailId().equals(Integer.valueOf(ecmArtworkNodesDTO.getItems()))) {
                        ecmArtworkNodesDTO.setVideoUrl(v.getVideoUrl());
                        ecmArtworkNodesDTO.setItemsBakText(v.getItemsBakText());
                    }
                }
            }
        });


        return ResponseDTO.ok(SUCCESS, artworkNodesDTOS);
    }


    /**
     * @param: [ecmArtworkNodesVo]
     * @return: void
     * @author: cxd
     * @Date: 2020/8/5
     * ?????? : ?????? EcmArtworkNodesVo ???????????????
     * ???????????? ???????????? ???????????????
     */
    private void saveArtwork(EcmArtworkNodesVo ecmArtworkNodesVo) {
        //?????????????????????????????????????????????????????????????????? ??? ?????????????????????

        if (ecmArtworkNodesVo.getPkDetailId() == null) {
//            ecmArtworkNodesVo.set
            ecmArtworkNodesDao.insertSelective(ecmArtworkNodesVo);
        }
        //??? ?????? ?????? ????????????????????? ?????? ?????? ????????? ???????????? id ??? ???????????? ????????? ??????  saveArtwork????????????????????????????????????
        if (!CollectionUtils.isEmpty(ecmArtworkNodesVo.getNodesVos())) {
            ecmArtworkNodesVo.getNodesVos().forEach(node -> {
                node.setParentId(ecmArtworkNodesVo.getPkDetailId());
                saveArtwork(node);
            });
        }
        // ????????????????????? ????????? ??????????????? ??????????????? ?????? ??????????????? ???????????? ??? ?????????????????? ???
        if (ecmArtworkNodesVo.getIsleaf() != null) {
            // ?????????????????? ????????????
            ecmArtworkNodesVo.setIsleaf(null);
            ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodesVo);
        }

    }


}

