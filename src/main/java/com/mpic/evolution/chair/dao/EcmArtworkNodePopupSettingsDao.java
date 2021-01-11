package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodePopupSettings;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkEndingsVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodePopupSettingsVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcmArtworkNodePopupSettingsDao {
    int deleteByPrimaryKey(Integer pkNodePopupSettingsId);

    int insert(EcmArtworkNodePopupSettings record);

    int insertSelective(EcmArtworkNodePopupSettings record);

    EcmArtworkNodePopupSettings selectByPrimaryKey(Integer pkNodePopupSettingsId);

    int updateByPrimaryKeySelective(EcmArtworkNodePopupSettings record);

    int updateByPrimaryKey(EcmArtworkNodePopupSettings record);

    List<EcmArtworkNodePopupSettingsVO> selectByArtworkNodeList(@Param("list") List<EcmArtworkNodesVo> list);

    EcmArtworkNodePopupSettingsVO selectByArtworkNodeId(Integer fkNodeId);

    int updateNameByArtWorkSelective(EcmArtworkNodePopupSettingsVO ecmArtworkNodePopupSettingsVO);

    int insertSelectiveList(List<EcmArtworkNodePopupSettings> ecmArtworkNodePopupSettingsList);

    List<EcmArtworkNodePopupSettingsVO> selectByEndingList(@Param("list") List<EcmArtworkEndingsVO> ecmArtworkEndingsVOList);
}
