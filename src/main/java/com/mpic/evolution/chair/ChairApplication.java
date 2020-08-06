package com.mpic.evolution.chair;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(basePackages =  {"com.mpic.evolution.chair.dao"})
@EnableTransactionManagement
@SpringBootApplication
public class ChairApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChairApplication.class, args);
    }

}
