package com.finn.gulimall.search;

import com.finn.common.validator.group.QcloudGroup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;

@EnableRedisHttpSession
@EnableFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class GulimallSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallSearchApplication.class, args);
    }

}