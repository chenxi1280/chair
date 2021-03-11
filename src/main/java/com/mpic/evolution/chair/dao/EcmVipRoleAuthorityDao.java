package com.mpic.evolution.chair.dao;



import com.mpic.evolution.chair.pojo.entity.EcmVipRoleAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcmVipRoleAuthorityDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmVipRoleAuthority record);

    int insertSelective(EcmVipRoleAuthority record);

    EcmVipRoleAuthority selectByPrimaryKey(Integer pkId);

    List<EcmVipRoleAuthority> selectByEcmVipRoleAuthority(EcmVipRoleAuthority record);

    int updateByPrimaryKeySelective(EcmVipRoleAuthority record);

    int updateByPrimaryKey(EcmVipRoleAuthority record);
}
