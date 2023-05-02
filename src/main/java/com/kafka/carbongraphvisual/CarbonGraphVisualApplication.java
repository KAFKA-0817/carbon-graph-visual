package com.kafka.carbongraphvisual;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kafka.carbongraphvisual.domain.mapper")
public class CarbonGraphVisualApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarbonGraphVisualApplication.class, args);
    }

}
