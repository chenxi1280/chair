package com.mpic.evolution.chair.service;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;

public interface EcmVipService {
    JSONObject getUserVipInfo(Integer fkUserId);
}
