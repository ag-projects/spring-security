package com.agharibi.springsecurity.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        UserDetails userDetails =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("user")
                        .roles("USER").build();

        auth.inMemoryAuthentication().withUser(userDetails);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/delete/**")
                .hasAuthority("ADMIN");

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .loginProcessingUrl("/doLogin")
                .and().httpBasic();
    }
    
}
