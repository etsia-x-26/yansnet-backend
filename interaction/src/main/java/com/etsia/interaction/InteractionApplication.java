package com.etsia.interaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;            
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.etsia.common.infrastructure.entities")              
@EnableJpaRepositories("com.etsia.interaction.infrastructure.repository")
@SpringBootApplication
public class InteractionApplication {

    public static void main(String[] args) {
        SpringApplication.run(InteractionApplication.class, args);
    }

}
