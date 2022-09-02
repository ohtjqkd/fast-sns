package com.fastcampus.fastsns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FastSnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastSnsApplication.class, args);
    }

}
