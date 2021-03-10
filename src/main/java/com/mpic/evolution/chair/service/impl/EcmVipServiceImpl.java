package com.mpic.evolution.chair.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mpic.evolution.chair.dao.EcmVipAuthorityDao;
import com.mpic.evolution.chair.dao.EcmVipRoleAuthorityDao;
import com.mpic.evolution.chair.dao.EcmVipRoleDao;
import com.mpic.evolution.chair.dao.EcmVipUserInfoDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmVipAuthority;
import com.mpic.evolution.chair.pojo.entity.EcmVipRole;
import com.mpic.evolution.chair.pojo.entity.EcmVipRoleAuthority;
import com.mpic.evolution.chair.pojo.entity.EcmVipUserInfo;
import com.mpic.evolution.chair.service.EcmVipService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EcmVipServiceImpl implements EcmVipService {
    @Resource
    EcmVipAuthorityDao ecmVipAuthorityDao;

    @Resource
    EcmVipRoleDao ecmVipRoleDao;

    @Resource
    EcmVipRoleAuthorityDao ecmVipRoleAuthorityDao;

    @Resource
    EcmVipUserInfoDao ecmVipUserInfoDao;

    public JSONObject getUserVipInfo(Integer fkUserId) {
        JSONObject data = new JSONObject();
        EcmVipUserInfo ecmVipUserInfo = new EcmVipUserInfo();
        ecmVipUserInfo.setFkUserid(fkUserId);
        ecmVipUserInfo = ecmVipUserInfoDao.selectByUserInfo(ecmVipUserInfo);
        Integer fkVipRoleId = ecmVipUserInfo.getFkVipRoleId();
        EcmVipRole ecmVipRole = ecmVipRoleDao.selectByPrimaryKey(fkVipRoleId);
        EcmVipRoleAuthority ecmVipRoleAuthority = new EcmVipRoleAuthority();
        ecmVipRoleAuthority.setFkVipRoleId(fkVipRoleId);
        ecmVipRoleAuthority.setVipRoleDescribe(ecmVipRole.getVipRoleDescribe());
        List<EcmVipRoleAuthority> ecmVipRoleAuthorities = ecmVipRoleAuthorityDao.selectByEcmVipRoleAuthority(ecmVipRoleAuthority);
        List<EcmVipAuthority> ecmVipAuthorities = ecmVipAuthorityDao.selectByAll();
        ArrayList<EcmVipRoleAuthority> list = new ArrayList<>();
        ecmVipAuthorities.stream().forEach(obj->{
            EcmVipRoleAuthority currentObj = new EcmVipRoleAuthority();
            currentObj.setFkVipAuthorityId(obj.getPkAuthorityId());
            currentObj.setVipAuthorityDescribe(obj.getVipAuthorityDescribe());
            List<EcmVipRoleAuthority> ecmVipRoleAuthoritiesList = ecmVipRoleAuthorityDao.selectByEcmVipRoleAuthority(currentObj);
            list.add(ecmVipRoleAuthoritiesList.get(0));
        });
        data.put("role",ecmVipRole.getVipRoleDescribe());
        data.put("authority",ecmVipRoleAuthorities);
        data.put("allAuthority",list);
        return data;
    }
}
