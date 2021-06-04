package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkBroadcastHotVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkEndingsVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashSet;
import java.util.List;

public interface EcmArtworkDao {
    int deleteByPrimaryKey(Integer pkArtworkId);

    int insert(EcmArtwork record);

    int insertSelective(EcmArtwork record);

    EcmArtwork selectByPrimaryKey(Integer pkArtworkId);

    int updateByPrimaryKeySelective(EcmArtwork record);

    int updateByPrimaryKey(EcmArtwork record);

    List<EcmArtworkVo> selectArtWorks(EcmArtWorkQuery ecmArtWorkQuery);

    List<EcmArtwork> selectDeletedArtWorks(EcmArtwork record);

    List<EcmArtworkVo> selectFindArtWorks(@Param("ids") List<EcmArtworkBroadcastHotVO> list);

    List<EcmArtworkVo> selectFindSortArtWorks(EcmArtWorkQuery ecmArtWorkQuery);

	List<EcmArtworkVo> selectArtWorksByWxUser(EcmArtWorkQuery ecmArtWorkQuery);

    List<EcmArtworkVo> selectSearchArtworkName(EcmArtWorkQuery ecmArtWorkQuery);

    List<EcmArtworkVo> selectArtWorkListByEcmArtWorkQuery(EcmArtWorkQuery ecmArtWorkQuery);

    Integer selectArtWorkCountByEcmArtWorkQuery(EcmArtWorkQuery ecmArtWorkQuery);

    int updateEndingsByArtworkId(int fkArtworkId);

    int updateEndingsByArtwork(EcmArtworkVo ecmArtworkVo);

    List<EcmArtworkVo> selectArtWorksAll();

    Integer selectByVideoCode(String fileId);

    /**
     * @author SJ
     */
    int deleteByStatus(Short artworkStatus);

    List<Integer> selectByArtworkIds(HashSet<Integer> artworkIds);
}
