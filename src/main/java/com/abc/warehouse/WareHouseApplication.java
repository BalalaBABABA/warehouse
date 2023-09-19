package com.abc.warehouse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.abc.warehouse.mapper")
public class WareHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(WareHouseApplication.class, args);
    }

}
