package com.finn.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
* 1.整合mybatis-plus
*   1) 导入依赖
*   2) 配置
*       1.配置数据源
*           1) 导入数据库驱动
*           2) application.yml配置
*       2.配置mybatis-plus
*           1) 使用MapperScan
*           2) SQL映射文件位置
* */
@MapperScan("com.finn.gulimall.product.dao")
@SpringBootApplication
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
