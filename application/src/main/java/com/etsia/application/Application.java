package com.etsia.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.etsia")
@EnableJpaRepositories(basePackages = "com.etsia")
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = "com.etsia")
public class Application {



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
