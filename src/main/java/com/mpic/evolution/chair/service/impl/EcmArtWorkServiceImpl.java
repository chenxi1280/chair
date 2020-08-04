package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.common.returnvo.ReturnVo;
import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.service.EcmArtWorkService;
import com.mpic.evolution.chair.util.TreeUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class EcmArtWorkServiceImpl implements EcmArtWorkService {
    @Resource
    EcmArtworkDao ecmArtworkDao;
    @Resource
    EcmArtworkNodesDao ecmArtworkNodesDao;

    @Override
    public ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {

        return   ResponseDTO.ok("success",ecmArtworkDao.selectArtWorks(ecmArtWorkQuery));
    }

    @Override
    public ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery) {
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtWorkQuery.getPkArtworkId());
        if (ecmArtwork==null){
            return ResponseDTO.fail("查询id为空");
        }
        List<EcmArtworkNodesVo>  list = ecmArtworkNodesDao.selectByArtWorkId(ecmArtWorkQuery.getPkArtworkId());
        return ResponseDTO.ok("success", TreeUtil.buildTree(list).get(0));
    }

    @Override
    public ResponseDTO addArtWorkNode(EcmArtworkNodes ecmArtworkNodes) {
        return ResponseDTO.get(1 == ecmArtworkNodesDao.insert(ecmArtworkNodes));
    }

    @Override
    public ResponseDTO addArtWork(EcmArtworkNodesVo ecmArtworkNodesVo) {


        classToTreeList(ecmArtworkNodesVo);


        return null;
    }

    private List<EcmArtworkNodes> classToTreeList(EcmArtworkNodesVo ecmArtworkNodesVo){

        List<EcmArtworkNodes> addlist = new ArrayList<>();
        List<EcmArtworkNodes> updatalist = new ArrayList<>();

        test(ecmArtworkNodesVo, addlist, updatalist);

        Integer i = ecmArtworkNodesDao.insertList(addlist);


        return null;
    }

    private void test(EcmArtworkNodesVo ecmArtworkNodesVo, List<EcmArtworkNodes> addlist, List<EcmArtworkNodes> updatalist) {

        List<EcmArtworkNodesVo> nodesVos = ecmArtworkNodesVo.getNodesVos();

        if (!CollectionUtils.isEmpty(nodesVos) || ecmArtworkNodesVo.getPkDetailId() != null){
            nodesVos.forEach( node -> {
                test(node,addlist,updatalist);
            });
        }else {

            ecmArtworkNodesDao.insertSelective(ecmArtworkNodesVo);

            test(ecmArtworkNodesVo,addlist,updatalist);

        }


        nodeClassification(ecmArtworkNodesVo, addlist, updatalist);
    }

    private void nodeClassification(EcmArtworkNodesVo ecmArtworkNodesVo, List<EcmArtworkNodes> addlist, List<EcmArtworkNodes> updatalist) {


        if (ecmArtworkNodesVo.getPkDetailId() != null){
            if (ecmArtworkNodesVo.getIsleaf() != null){
                updatalist.add(ecmArtworkNodesVo);
            }
        }else {
            addlist.add(ecmArtworkNodesVo);
        }
    }


}

