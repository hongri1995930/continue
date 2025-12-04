package com.continuehub.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.continuehub.server.repository")
public class HubServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HubServerApplication.class, args);
    }
}
