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
import com.mpic.evolution.chair.util.VipDateUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

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

    @Override
    public JSONObject getUserVipInfo(Integer fkUserId) {
        JSONObject data = new JSONObject();
        EcmVipUserInfo ecmVipUserInfo = new EcmVipUserInfo();
        ecmVipUserInfo.setFkUserid(fkUserId);
        List<EcmVipUserInfo> ecmVipUserInfos = ecmVipUserInfoDao.selectByUserInfo(ecmVipUserInfo);
        //如果list 为空说明是普通用户
        if(ecmVipUserInfos == null || ecmVipUserInfos.size() < 1){
            List<EcmVipAuthority> ecmVipAuthorities = ecmVipAuthorityDao.selectByAll();
            ArrayList<EcmVipRoleAuthority> list = new ArrayList<>();
            ecmVipAuthorities.stream().forEach(obj->{
                EcmVipRoleAuthority currentObj = new EcmVipRoleAuthority();
                currentObj.setFkVipAuthorityId(obj.getPkAuthorityId());
                currentObj.setVipAuthorityDescribe(obj.getVipAuthorityDescribe());
                List<EcmVipRoleAuthority> ecmVipRoleAuthoritiesList = ecmVipRoleAuthorityDao.selectByEcmVipRoleAuthority(currentObj);
                // 传递权限对应角色要求最低得数据
                EcmVipRoleAuthority lowRoleAuthority = this.getLowRoleAuthority(ecmVipRoleAuthoritiesList);
                list.add(lowRoleAuthority);
            });
            data.put("role",null);
            data.put("authority",null);
            data.put("allAuthority",list);
            return data;
        }else{
            //组装所有权限对应低等级得角色信息
            List<EcmVipAuthority> ecmVipAuthorities = ecmVipAuthorityDao.selectByAll();
            ArrayList<EcmVipRoleAuthority> list = new ArrayList<>();
            ecmVipAuthorities.stream().forEach(obj->{
                EcmVipRoleAuthority currentObj = new EcmVipRoleAuthority();
                currentObj.setFkVipAuthorityId(obj.getPkAuthorityId());
                currentObj.setVipAuthorityDescribe(obj.getVipAuthorityDescribe());
                List<EcmVipRoleAuthority> ecmVipRoleAuthoritiesList = ecmVipRoleAuthorityDao.selectByEcmVipRoleAuthority(currentObj);
                EcmVipRoleAuthority lowRoleAuthority = this.getLowRoleAuthority(ecmVipRoleAuthoritiesList);
                list.add(lowRoleAuthority);
            });
            // 获取用户的有效身份 有效身份是指在有效期内权限最高的身份
            ecmVipUserInfo = this.getEffectiveVipInfo(ecmVipUserInfos);
            //若查询角色会员信息为空 则说明是非会员得普通用户
            if(ecmVipUserInfo == null){
                EcmVipRole ecmVipRole = ecmVipRoleDao.selectByPrimaryKey(3);
                EcmVipRoleAuthority ecmVipRoleAuthority = new EcmVipRoleAuthority();
                ecmVipRoleAuthority.setFkVipRoleId(ecmVipRole.getPkRoleId());
                ecmVipRoleAuthority.setVipRoleDescribe(ecmVipRole.getVipRoleDescribe());
                List<EcmVipRoleAuthority> ecmVipRoleAuthorities = ecmVipRoleAuthorityDao.selectByEcmVipRoleAuthority(ecmVipRoleAuthority);
                data.put("role",ecmVipRole.getVipRoleDescribe());
                data.put("authority",ecmVipRoleAuthorities);
                data.put("allAuthority",list);
            }else{
                Integer fkVipRoleId = ecmVipUserInfo.getFkVipRoleId();
                EcmVipRole ecmVipRole = ecmVipRoleDao.selectByPrimaryKey(fkVipRoleId);
                EcmVipRoleAuthority ecmVipRoleAuthority = new EcmVipRoleAuthority();
                ecmVipRoleAuthority.setFkVipRoleId(fkVipRoleId);
                ecmVipRoleAuthority.setVipRoleDescribe(ecmVipRole.getVipRoleDescribe());
                List<EcmVipRoleAuthority> ecmVipRoleAuthorities = ecmVipRoleAuthorityDao.selectByEcmVipRoleAuthority(ecmVipRoleAuthority);
                data.put("role",ecmVipRole.getVipRoleDescribe());
                data.put("authority",ecmVipRoleAuthorities);
                data.put("allAuthority",list);
            }
        }
        return data;
    }

    private EcmVipUserInfo getEffectiveVipInfo(List<EcmVipUserInfo> ecmVipUserInfos){
        HashMap<Integer,Integer> map = new HashMap<>();
        //如果集合只有一个元素就不用判断了
        if(ecmVipUserInfos.size() > 1){
            //筛选出过期的用户信息
            for (int i = 0; i < ecmVipUserInfos.size();i++){
                LocalDateTime now = LocalDateTime.now();
                Date vipEndTime = ecmVipUserInfos.get(i).getVipEndTime();
                LocalDateTime endTime = VipDateUtil.formatToLocalDateTime(vipEndTime);
                if(endTime.isBefore(now)){
                    ecmVipUserInfos.remove(i);
                }else{
                    map.put(ecmVipUserInfos.get(i).getFkVipRoleId(),i);
                }
            }
            //按照高等级到低等级获取
            if(map.containsKey(2)){
                return ecmVipUserInfos.get(map.get(2));
            }else if(map.containsKey(1)){
                return ecmVipUserInfos.get(map.get(1));
            }else{
                return null;
            }
        }else{
            return ecmVipUserInfos.get(0);
        }
    }

    private EcmVipRoleAuthority getLowRoleAuthority(List<EcmVipRoleAuthority> ecmVipRoleAuthoritiesList){
        HashMap<Integer,Integer> map = new HashMap<>();
        //如果集合只有一个元素就不用判断了
        if(ecmVipRoleAuthoritiesList.size() > 1){
            //筛选出过期的用户信息
            for (int i = 0; i < ecmVipRoleAuthoritiesList.size();i++){
                map.put(ecmVipRoleAuthoritiesList.get(i).getFkVipRoleId(),i);
            }
            //按照低等级到高等级获取
            if(map.containsKey(1)){
                return ecmVipRoleAuthoritiesList.get(map.get(1));
            }else if(map.containsKey(2)){
                return ecmVipRoleAuthoritiesList.get(map.get(2));
            }else{
                return null;
            }
        }else{
            return ecmVipRoleAuthoritiesList.get(0);
        }
    }
}
