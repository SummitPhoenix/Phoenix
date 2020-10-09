package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = {"com.*"})
@MapperScan(basePackages = {"com.sparkle.mapper"})
@EnableScheduling
@Configuration
public class PhoenixApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhoenixApplication.class, args);
    }

}
