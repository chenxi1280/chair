package com.mpic.evolution.chair;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(basePackages =  {"com.mpic.evolution.chair.dao"})
@EnableTransactionManagement
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class ChairApplication  extends SpringBootServletInitializer {
    Logger logger = LoggerFactory.getLogger(ChairApplication.class);

    /**
     * 这里是配置服务器的时区，如果服务器的时区和数据库的时区不一致，
     * 那么查询出来的事件和数据库的时间会有差距，一般相差8个小时，就是因为时区相差8小时造成的，中国时区是Asia/Shanghai
     * 而mysql的默认时区是外国的，就会造成问题，所以这里要配置，另外，在服务器上面
     */
    @PostConstruct
    void setTimezone() {
        logger.warn("服务器Timezone默认时区是：" + TimeZone.getDefault().getID());
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ChairApplication.class);

    }

    public static void main(String[] args) {
        SpringApplication.run(ChairApplication.class, args);
    }

}
