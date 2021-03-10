package com.mpic.evolution.chair.pojo.tencent.wx.pay;

import lombok.Data;

/**
 * @author by cxd
 * @Classname WxPayDTOResource
 * @Description TODO
 * @Date 2021/3/9 17:20
 */
@Data
public class WxPayResourceDTO {
    private String algorithm;
    private String ciphertext;
    private String nonce;
    private String original_type;
    private String associated_data;
}
