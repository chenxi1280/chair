package com.mpic.evolution.chair.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.core.signature.CosSignatureUtils;
import com.mpic.evolution.chair.core.signature.SignatureUtil;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;

/**
 *
 * 类名:  SignatureController
 * @author Xuezx
 * @date 2020/8/15 11:25
 * 描述: 用于云点播和云对象存储等获取签名
 *
 */
@Controller
public class SignatureController extends BaseController {

    @RequestMapping("/artworkMaking/findSingature")
    @ResponseBody
    public ResponseDTO getConfirmCode() {
        return ResponseDTO.ok("获取验证码成功", SignatureUtil.getUploadSignature());
    }


    /**
     * 获取签名
     * @author xuezx
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/artworkMaking/findCosSingature")
    public ResponseDTO findCosSingature(){
        return ResponseDTO.ok("获取验证码成功", CosSignatureUtils.getSignature().toString());
    }
}
