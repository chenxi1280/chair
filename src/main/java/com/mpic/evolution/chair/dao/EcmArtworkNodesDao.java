package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkEndingsVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface EcmArtworkNodesDao {
    int deleteByPrimaryKey(Integer pkDetailId);

    int insert(EcmArtworkNodes record);

    int insertSelective(EcmArtworkNodes record);

    EcmArtworkNodes selectByPrimaryKey(Integer pkDetailId);

    int updateByPrimaryKeySelective(EcmArtworkNodes record);

    int updateByPrimaryKey(EcmArtworkNodes record);

    List<EcmArtworkNodesVo> selectByArtWorkId(Integer pkArtworkId);

    Integer insertList(@Param("list") List<EcmArtworkNodesVo> addlist);

    int removeByPrimaryKey(Integer pkDetailId);

    List<EcmArtworkNodesVo> selectByVideoCode(String fileId);

    /**
     * @param: [ecmArtworkNodesList]
     * @return: int
     * @author: cxd
     * @Date: 2020/12/15
     * 描述 : 视频回调批量修改节点视频url 状态等信息
     */
    int updateByEcmArtworkNodesList(List<EcmArtworkNodesVo> ecmArtworkNodesList);

    int updateVideoUrlPrimaryEcmArtworkNode(EcmArtworkNodes ecmArtworkNodes);

    int updateNodeNumberFlag(@Param("fkArtworkId") Integer fkArtworkId);

    int removeByNodeIds(@Param("list")List<Integer> ids);

    int deleteByEcmArtworkEndingsList(@Param("list") List<EcmArtworkEndingsVO> ecmArtworkEndingsVOList);

    Integer insertEndingList(@Param("list") List<EcmArtworkNodes> ecmArtworkNodesVoList);

    void deleteEcmArtworkEndingsByArtworkId(int fkArtworkId);

    int updateSelectiveEndingList(@Param("list")List<EcmArtworkNodes> ecmArtworkNodesVoList);

    int deleteByNodeId(Integer fkNodeId);

    int deleteByPrimaryKeyList( @Param("list") List<EcmArtworkEndingsVO> ecmArtworkEndings);

    List<EcmArtworkNodesVo> selectByArtWorkList(@Param("list") List<EcmArtworkVo> ecmArtworkVoList);

    int updatePopupSetting(EcmArtworkNodes pkDetailId);

    int updateEndingConditionByArtworkId(EcmArtworkNodes ecmArtworkNodes);

    int updateArtworkNodeBuoy(EcmArtworkNodesVo ecmArtworkNodesVo);

    int updateArtworkNodeBuoyByFkNodeId(Integer parentId);

    int updateLocationByPrimaryKeyBuoy(EcmArtworkNodesVo ecmArtworkNodes);

    int updateArtworkNode(EcmArtworkNodesVo ecmArtworkNodesVo);

    int updateMigrateByEcmArtworkNodesList(@Param("list") List<EcmArtworkNodesVo> ecmArtworkNodesList);

    int updatePrivateVideoUrl(EcmArtworkNodes nodes);

    /**
     * 根据artworkid 查出所有已删除的子节点
     * @author SJ
     * @param ecmArtworkIds
     * @return
     */
    List<String> selectDeletedArtWorkNodesByArtworkIds(@Param("ecmArtworkIds") ArrayList<Integer> ecmArtworkIds);

    /**
     * 根据节点状态 查出所有已删除的子节点
     * @author SJ
     * @param record
     * @return
     */
    List<String> selectDeletedArtWorkNodesByNodesStatus(EcmArtworkNodes record);

    /**
     * @author SJ
     */
    int deleteByIsDelete(String isDeleted);
}
