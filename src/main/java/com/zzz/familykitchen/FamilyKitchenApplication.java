package com.zzz.familykitchen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zzz.familykitchen.mapper")
public class FamilyKitchenApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilyKitchenApplication.class, args);
    }

}
