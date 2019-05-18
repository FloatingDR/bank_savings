package com.bank.savings;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author taylor
 * @date 2019.5.18
 */
@MapperScan("com.bank.savings.mapper")
@EnableTransactionManagement
@SpringBootApplication
public class SavingsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SavingsApplication.class, args);
    }
}
