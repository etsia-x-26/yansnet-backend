package com.etsia.channel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = {
        "com.etsia.channel"
})
public class ChannelApplication {



    public static void main(String[] args) {
        SpringApplication.run(ChannelApplication.class, args);
    }

}
