package com.agharibi.springsecurity.spring;

import com.agharibi.springsecurity.persistence.UserRepository;
import com.agharibi.springsecurity.web.model.User;
import com.sun.istack.internal.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.agharibi.springsecurity")
@EnableJpaRepositories("com.agharibi.springsecurity")
@EntityScan("com.agharibi.springsecurity.web.model")
public class SpringsecurityApplication {


	public static void main(String[] args) {
		SpringApplication.run(new Class[]{SpringsecurityApplication.class, SecurityConfig.class}, args);

	}
}
