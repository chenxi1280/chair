package com.mpic.evolution.chair.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.mpic.evolution.chair.pojo.dto.EcmArtworkNodesDTO;
import com.mpic.evolution.chair.pojo.entity.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import com.mpic.evolution.chair.dao.EcmArtworkBroadcastHistoryDao;
import com.mpic.evolution.chair.dao.EcmArtworkBroadcastHotDao;
import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.dao.EcmUserDao;
import com.mpic.evolution.chair.dao.ProcessMediaByProcedureDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkBroadcastHotVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.pojo.vo.MediaByProcedureVo;
import com.mpic.evolution.chair.service.EcmArtWorkService;
import com.mpic.evolution.chair.util.RandomUtil;
import com.mpic.evolution.chair.util.TreeUtil;


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
    EcmArtworkBroadcastHistoryDao ecmArtworkBroadcastHistoryDao;
    @Resource
    EcmArtworkBroadcastHotDao ecmArtworkBroadcastHotDao;

    @Override
    public ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {

        return ResponseDTO.ok("success", ecmArtworkDao.selectArtWorks(ecmArtWorkQuery));
    }

    @Override
    public ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery) {
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtWorkQuery.getPkArtworkId());
        if (ecmArtwork == null) {
            return ResponseDTO.fail("查询id为空");
        }

        List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(ecmArtWorkQuery.getPkArtworkId());

        for (EcmArtworkNodesVo node : list) {
            if (!StringUtils.isEmpty(node.getItems()) && node.getALevel() != null) {
                if (node.getALevel().equals(1)) {
                    //优化！ 跳转
                    for (EcmArtworkNodesVo ecmArtworkNodesVo : list) {

                        if (ecmArtworkNodesVo.getPkDetailId().equals(Integer.valueOf(node.getItems()))) {
                            EcmArtworkNodes ecm = new EcmArtworkNodes();
                            //深克隆
                            // ecm = ecmArtworkNodesVo.clone();
                            ecm.setPkDetailId(ecmArtworkNodesVo.getPkDetailId());
                            ecm.setVideoText(ecmArtworkNodesVo.getVideoText());
                            ecm.setCssVo(ecmArtworkNodesVo.getCssVo());
                            ecm.setFkArtworkId(ecmArtwork.getPkArtworkId());
                            ecm.setItemsBakText(ecmArtworkNodesVo.getItemsBakText());
                            node.setLinkNode(ecm);
                        }
                    }
                }
            }
        }
        List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> !"Y".equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());

        if (list.isEmpty()) {
            return ResponseDTO.fail("查询id无子节点");
        }
        return ResponseDTO.ok("success", TreeUtil.buildTree(collect).get(0));
    }

    @Override
    public ResponseDTO saveArtWorkNode(EcmArtworkNodesVo ecmArtworkNodes) {

        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodes.getFkArtworkId());
        if (!ecmArtwork.getFkUserid().equals(ecmArtworkNodes.getFkUserId())) {
            return ResponseDTO.fail("非法访问");
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
        if ("Y".equals(ecmArtworkNodes.getIsleaf())) {
            ecmArtworkNodes.setIsleaf("");
            ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodes);
            // 跳转节点的数据
            if (!StringUtils.isEmpty(ecmArtworkNodes.getItems())) {
                EcmArtworkNodes ecmArtworkNode = ecmArtworkNodesDao.selectByPrimaryKey(Integer.valueOf(ecmArtworkNodes.getItems()));
                ecmArtworkNodes.setLinkNode(ecmArtworkNode);
            }
            return ResponseDTO.ok("成功", ecmArtworkNodes);
        }
        //默认图片地址
        if( !ecmArtworkNodes.getALevel().equals(1)) {
            ecmArtworkNodes.setItemsBakText("https://sike-1259692143.cos.ap-chongqing.myqcloud.com/baseImg/1599646010668nodeImgUrl.png");
        }
        if (StringUtils.isEmpty(ecmArtworkNodes.getCssVo())){
            ecmArtworkNodes.setCssVo("未命名标题就是标\n准的15字");
        }
        if (StringUtils.isEmpty(ecmArtworkNodes.getVideoText())){
            ecmArtworkNodes.setVideoText("未命名选项");
        }
        if (1 == ecmArtworkNodesDao.insert(ecmArtworkNodes)) {
            return ResponseDTO.ok("success", ecmArtworkNodes);
        }
        return ResponseDTO.fail("正在保存上一个节点数据");
    }


    @Override
    @Transactional
    public ResponseDTO addArtWork(EcmArtworkNodesVo ecmArtworkNodesVo) {
        // 在 saveArtwork 中 出现 异常后 进行事务回滚操作 并返回错误
        try {
            saveArtwork(ecmArtworkNodesVo);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseDTO.fail("error");
        }

        return ResponseDTO.ok("success");
    }

    @Override
    public ResponseDTO removeNode(EcmArtworkNodesVo ecmArtworkNodesVo) {
        if (ecmArtworkNodesVo.getFkArtworkId() == null) {
            return ResponseDTO.fail("作品错误");
        }
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodesVo.getFkArtworkId());
        if (!ecmArtwork.getFkUserid().equals(ecmArtworkNodesVo.getFkUserId())) {
            return ResponseDTO.fail("非法访问");
        }

        if (ecmArtworkNodesVo.getPkDetailId() != null) {
            return ResponseDTO.get(1 == ecmArtworkNodesDao.removeByPrimaryKey(ecmArtworkNodesVo.getPkDetailId()));
        }
        return ResponseDTO.fail("网络错误");
    }


    @Override
    public ResponseDTO getFindArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {

        List<EcmArtworkBroadcastHotVO> ecmArtworkBroadcastHotVOS = ecmArtworkBroadcastHotDao.selectFindAll(ecmArtWorkQuery);
        if (CollectionUtils.isEmpty(ecmArtworkBroadcastHotVOS)) {
            return ResponseDTO.fail("无数据");
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
        EcmArtworkVo ecmArtworkVo = new EcmArtworkVo();
        ecmArtworkVo.setUserName("ad");
        ecmArtworkVo.setLogoPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599039415911&di=41f622b164548552e3e407e5e955b940&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F52%2F52%2F01200000169026136208529565374.jpg");
        ecmArtworkVo.setArtworkName("好产品XXX造");
        ecmArtworkVo.setCode("ad");
        list.add(1, ecmArtworkVo);
        return ResponseDTO.ok("sucess", list);
    }


    @Override
    public ResponseDTO getFindSortArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {
        List<EcmArtworkVo> list = ecmArtworkDao.selectFindSortArtWorks(ecmArtWorkQuery);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseDTO.fail("无数据");
        }
        List<EcmUserVo> userVoList = ecmUserDao.selectUserByEcmArtworkList(list);
        List<EcmArtworkBroadcastHotVO> ecmArtworkBroadcastHotVOS = ecmArtworkBroadcastHotDao.selectEcmArtworkList(list);
        list.forEach(ecmArtworkVo -> {
            userVoList.forEach(ecmUserVo -> {
                // 设置作者名字
                if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())) {
                    ecmArtworkVo.setUserName(ecmUserVo.getUsername());
                }
                // 设置用户头像
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
                // 没有在热度表中 作品 随机 热度
                if (ecmArtworkVo.getHotCount() == null) {
                    ecmArtworkVo.setHotCount(RandomUtil.getCode(3));
                }

            });
        });
        //插入广告
        EcmArtworkVo ecmArtworkVo = new EcmArtworkVo();
        ecmArtworkVo.setUserName("ad");
        ecmArtworkVo.setLogoPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599039415911&di=41f622b164548552e3e407e5e955b940&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F52%2F52%2F01200000169026136208529565374.jpg");
        ecmArtworkVo.setArtworkName("好产品XXX造");
        ecmArtworkVo.setCode("ad");
        list.add(1, ecmArtworkVo);
        return ResponseDTO.ok("sucess", list);
    }



    @Override
    public ResponseDTO getRankingArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {
        List<EcmArtworkBroadcastHotVO> ecmArtworkBroadcastHotVOS = ecmArtworkBroadcastHotDao.selectFindAll(ecmArtWorkQuery);
        if (CollectionUtils.isEmpty(ecmArtworkBroadcastHotVOS)) {
            return ResponseDTO.fail("无数据");
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

        return ResponseDTO.ok("sucess", list);
    }

    @Override
    public ResponseDTO search(EcmArtWorkQuery ecmArtWorkQuery) {
        List<EcmArtworkVo> list = ecmArtworkDao.selectSearchArtworkName(ecmArtWorkQuery);

        List<EcmUserVo> userVoList = ecmUserDao.selectUserByEcmArtworkList(list);

        List<EcmArtworkBroadcastHotVO> ecmArtworkBroadcastHotVOS = ecmArtworkBroadcastHotDao.selectEcmArtworkList(list);
        list.forEach(ecmArtworkVo -> {
            userVoList.forEach(ecmUserVo -> {
                // 设置作者名字
                if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())) {
                    ecmArtworkVo.setUserName(ecmUserVo.getUsername());
                }
                // 设置用户头像
                if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())) {
                    ecmArtworkVo.setUserName(ecmUserVo.getUsername());
                    if (!StringUtils.isEmpty(ecmUserVo.getUserLogoUrl())) {
                        ecmArtworkVo.setUserLogoUrl(ecmUserVo.getUserLogoUrl());
                    }
                }
                //设置热度
                ecmArtworkBroadcastHotVOS.forEach(ecmArtworkBroadcastHotVO -> {
                    if (ecmArtworkBroadcastHotVO.getFkArkworkId().equals(ecmArtworkVo.getPkArtworkId())) {
                        ecmArtworkVo.setHotCount(ecmArtworkBroadcastHotVO.getBroadcastCount());
                    }
                });
                // 没有在热度表中 作品 随机 热度
                if (ecmArtworkVo.getHotCount() == null) {
                    ecmArtworkVo.setHotCount(RandomUtil.getCode(3));
                }

            });
        });
        return  ResponseDTO.ok("sucess", list);
    }

    @Override
    public ResponseDTO getArtWorkNodes(EcmArtWorkQuery ecmArtWorkQuery) {

        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtWorkQuery.getPkArtworkId());
        if (ecmArtwork == null) {
            return ResponseDTO.fail("查询id为空");
        }

        List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(ecmArtWorkQuery.getPkArtworkId());

        Map<Integer, List<EcmArtworkNodesVo>> collect = list.stream().collect(Collectors.groupingBy(EcmArtworkNodes::getParentId));

        List<EcmArtworkNodesDTO> artworkNodesDTOS = new ArrayList<>();

        list.forEach( node -> {
            EcmArtworkNodesDTO ecmArtworkNodesDTO = new EcmArtworkNodesDTO();

            BeanUtils.copyProperties(node,ecmArtworkNodesDTO);

            ecmArtworkNodesDTO.setBrotherNode(collect.get(node.getParentId()));

            artworkNodesDTOS.add(ecmArtworkNodesDTO);
        });

        return ResponseDTO.ok("",artworkNodesDTOS);
    }


    /**
     * @param: [ecmArtworkNodesVo]
     * @return: void
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 保存 EcmArtworkNodesVo 对象的方法
     * 发生异常 会被上级 捕获并回滚
     */
    private void saveArtwork(EcmArtworkNodesVo ecmArtworkNodesVo) {
        //先进行判断是否有主见，没有主键则直接进行插入 并 获取到自增主键

        if (ecmArtworkNodesVo.getPkDetailId() == null) {
//            ecmArtworkNodesVo.set
            ecmArtworkNodesDao.insertSelective(ecmArtworkNodesVo);
        }
        //再 进行 节点 是否还有子节点 判断 有就 先设置 父节点的 id 再 进行循环 并再次 进行  saveArtwork（）方法调用（递归判断）
        if (!CollectionUtils.isEmpty(ecmArtworkNodesVo.getNodesVos())) {
            ecmArtworkNodesVo.getNodesVos().forEach(node -> {
                node.setParentId(ecmArtworkNodesVo.getPkDetailId());
                saveArtwork(node);
            });
        }
        // 是否为更新节点 判断（ 在节点不是 新建节点时 ，又 进行了节点 更新操作 时 进行更新操作 ）
        if (ecmArtworkNodesVo.getIsleaf() != null) {
            // 把前端传回的 标记清空
            ecmArtworkNodesVo.setIsleaf(null);
            ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodesVo);
        }

    }

}

