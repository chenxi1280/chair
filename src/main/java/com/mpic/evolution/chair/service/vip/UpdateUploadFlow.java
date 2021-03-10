package com.mpic.evolution.chair.service.vip;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UpdateUploadFlow implements PaymentVipService {
    @Override
    public boolean operationRelateToPayment(Integer number,Integer fkUserId) {
        System.out.println("3");
        return false;
    }
}
