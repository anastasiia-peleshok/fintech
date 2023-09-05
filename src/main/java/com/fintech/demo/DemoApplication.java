package com.fintech.demo;

import com.fintech.demo.configuration.ApplicationProperties;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableJpaRepositories
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(ApplicationProperties applicationProperties){
		return args-> System.out.println(applicationProperties);
	}
}