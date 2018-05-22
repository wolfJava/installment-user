package com.wolfman.installment.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "com.wolfman.installment.user.dal.mapper")
@EnableTransactionManagement
public class UserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserWebApplication.class,args);
    }

}
