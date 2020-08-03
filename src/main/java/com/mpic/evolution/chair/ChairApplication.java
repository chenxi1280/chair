package com.mpic.evolution.chair;

import org.mapstruct.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"com.mpic.evolution.dao"})
@SpringBootApplication
public class ChairApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChairApplication.class, args);
    }

}
