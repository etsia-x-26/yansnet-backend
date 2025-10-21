package com.etsia.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.etsia.auth","com.etsia.interaction","com.etsia.common.infrastructure.controller", "com.etsia.notification.infrastructure.controller", "com.etsia.post", "com.etsia.user.infrastructure.controller", "com.etsia.message.infrastructure.controller", "com.etsia.interaction.infrastructure.controller"})
@EntityScan(basePackages = {"com.etsia.common.infrastructure.entities", "com.etsia.auth.infrastructure.repository"})
public class Application {



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
