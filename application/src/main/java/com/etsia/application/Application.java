package com.etsia.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
		"com.etsia.auth","com.etsia.interaction","com.etsia.common.infrastructure.controller", "com.etsia.notification.infrastructure.controller", "com.etsia.post", "com.etsia.user.infrastructure.controller", "com.etsia.message.infrastructure.controller", "com.etsia.interaction.infrastructure.controller", "com.etsia.channel", "com.etsia"
})
@EnableJpaRepositories(basePackages = {
		"com.etsia.channel",
		"com.etsia.auth.infrastructure.repository",
		"com.etsia.user.infrastructure.repository",
		"com.etsia.group.infrastructure.repository",
		"com.etsia.comment.domain.repository",
		"com.etsia.post.infrastructure.repository",
		"com.etsia.interaction.infrastructure.repository"
})
public class Application {



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
