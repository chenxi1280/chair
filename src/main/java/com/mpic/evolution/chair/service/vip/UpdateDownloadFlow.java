package com.mpic.evolution.chair.service.vip;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UpdateDownloadFlow implements PaymentVipService {

    @Override
    public boolean operationRelateToPayment(Integer number,Integer fkUserId,String type) {
        System.out.println("2");
        return false;
    }
}
