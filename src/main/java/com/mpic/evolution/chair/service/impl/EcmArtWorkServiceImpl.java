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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author cxd
 */
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

    /**
     * @param: [ecmArtworkNodesVo]
     * @return: void
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 保存 EcmArtworkNodesVo 对象的方法
     *       发生异常 会被上级 捕获并回滚
     */
    private void saveArtwork(EcmArtworkNodesVo ecmArtworkNodesVo) {
        //先进行判断是否有主见，没有主键则直接进行插入 并 获取到自增主键
        if ( ecmArtworkNodesVo.getPkDetailId() == null){
            ecmArtworkNodesDao.insertSelective(ecmArtworkNodesVo);
        }
        //再 进行 节点 是否还有子节点 判断 有就 先设置 父节点的 id 再 进行循环 并再次 进行  saveArtwork（）方法调用（递归判断）
        if (!CollectionUtils.isEmpty(ecmArtworkNodesVo.getNodesVos()) ){
            ecmArtworkNodesVo.getNodesVos().forEach( node -> {
                node.setParentId(ecmArtworkNodesVo.getPkDetailId());
                saveArtwork(node);
            });
        }
        // 是否为更新节点 判断（ 在节点不是 新建节点时 ，又 进行了节点 更新操作 时 进行更新操作 ）
        if (ecmArtworkNodesVo.getIsleaf() != null){
            // 把前端传回的 标记清空
            ecmArtworkNodesVo.setIsleaf(null);
            ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodesVo);
        }

    }


}

