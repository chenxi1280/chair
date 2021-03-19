package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.MerchantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import static com.mpic.evolution.chair.common.returnvo.ErrorEnum.ERR_603;

/**
 * @author by cxd
 * @Classname MerchantController
 * @Description TODO
 * @Date 2021/3/1 9:17
 */
@Controller
@RequestMapping("merchant")
public class MerchantController extends BaseController {
    @Resource
    MerchantService merchantService;

    @RequestMapping("getMerchantUrl")
    @ResponseBody
    public ResponseDTO getMerchantUrl(@RequestBody EcmArtworkVo ecmArtworkVo) {

        Integer userIdByHandToken = getUserIdByHandToken();
        if (userIdByHandToken == null ){
            return ResponseDTO.fail(ERR_603.getText());
        }
        ecmArtworkVo.setFkUserid(userIdByHandToken);

        return  merchantService.getMerchantUrl(ecmArtworkVo);
    }





}
