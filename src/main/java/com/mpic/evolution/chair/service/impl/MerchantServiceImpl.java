package com.mpic.evolution.chair.service.impl;

import com.google.zxing.WriterException;
import com.mpic.evolution.chair.common.constant.SecretKeyConstants;
import com.mpic.evolution.chair.dao.EcmMerchantDao;
import com.mpic.evolution.chair.dao.EcmUserDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmMerchant;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.pojo.vo.EcmMerchantVO;
import com.mpic.evolution.chair.service.MerchantService;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.mpic.evolution.chair.common.constant.CommonField.INT_ONE;
import static com.mpic.evolution.chair.common.constant.H5Constants.H5_BASE_URL_TEST;

/**
 * @author by cxd
 * @Classname MerchantServiceImpl
 * @Description TODO
 * @Date 2021/3/1 9:29
 */
@Service
public class MerchantServiceImpl implements MerchantService {
    @Resource
    EcmMerchantDao ecmMerchantDao;
    @Resource
    EcmUserDao ecmUserDao;


    @Override
    public ResponseDTO getMerchantUrl(EcmArtworkVo ecmArtworkVo) {

        EcmMerchantVO ecmMerchant = ecmMerchantDao.selectByUserId(ecmArtworkVo.getFkUserid());
        EcmUser user = ecmUserDao.selectByPrimaryKey(ecmArtworkVo.getFkUserid());
        if ( ecmMerchant == null ){
            ecmMerchant = new EcmMerchantVO();
            ecmMerchant.setCreateTime(new Date());
            ecmMerchant.setFkUserId(ecmArtworkVo.getFkUserid());
            ecmMerchant.setMobilePhone(String.valueOf(user.getMobile()));
            ecmMerchant.setMerchantName(user.getUsername() + "的店铺");
            ecmMerchant.setOpenState(INT_ONE);
            ecmMerchant.setH5Url(H5_BASE_URL_TEST);
            ecmMerchant.setContactPhone(String.valueOf(user.getMobile()));
            ecmMerchantDao.insertSelective(ecmMerchant);
        }
        HashMap<String, Object> map = new HashMap<>(2);
        String url = ecmMerchant.getH5Url() + "?token=" + JWTUtil.sign(String.valueOf(ecmMerchant.getFkUserId()),
                user.getUsername(), SecretKeyConstants.JWT_SECRET_KEY);
        map.put("url",url);
//        try {
//            map.put("QRCode", QRCodeUtils.coachQRCode(url));
//        } catch (WriterException e) {
//            e.printStackTrace();
//            return ResponseDTO.ok(map);
//        }

        return ResponseDTO.ok(map);
    }
}
