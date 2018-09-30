package com.agharibi.springsecurity.spring;

import com.agharibi.springsecurity.persistence.InMemoryUserRepository;
import com.agharibi.springsecurity.persistence.UserRepository;
import com.agharibi.springsecurity.web.model.User;
import com.sun.istack.internal.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;

@SpringBootApplication
@ComponentScan("com.agharibi.springsecurity.web")
public class SpringsecurityApplication {

	@Bean
	public UserRepository userRepository() {
		return new InMemoryUserRepository();
	}

	@Bean
	public Converter<String, User> messageConverter() {
		return new Converter<String, User>() {
			@Nullable
			@Override
			public User convert(String id) {
				return userRepository().findUser(Long.valueOf(id));
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(new Class[]{SpringsecurityApplication.class, SecurityConfig.class}, args);

	}
}
