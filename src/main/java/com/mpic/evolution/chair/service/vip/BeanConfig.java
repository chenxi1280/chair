package com.mpic.evolution.chair.service.vip;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BeanConfig implements InitializingBean, ApplicationContextAware {
    private HashMap<String, PaymentVipService> querySeviceImpMap = new HashMap<>();
    private ApplicationContext applicationContext;

    //遍历所有接口将其放入map中
    public PaymentVipService createQueryService(String type){
        PaymentVipService paymentVipService = querySeviceImpMap.get(type);
        if(paymentVipService == null){
            return querySeviceImpMap.get("UpdateVipDate");
        }
        return paymentVipService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, PaymentVipService> beanMap = applicationContext.getBeansOfType(PaymentVipService.class);
        for (PaymentVipService serviceImpl : beanMap.values()) {
            String name = serviceImpl.getClass().getSimpleName();
            int index = name.indexOf("$");
            if(index<0){
                querySeviceImpMap.put(name, serviceImpl);
            }else {
                String string = name.substring(0, index);
                querySeviceImpMap.put(string, serviceImpl);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
