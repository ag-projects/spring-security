package com.agharibi.springsecurity.spring;

import com.agharibi.springsecurity.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfig() {
        super();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/delete/**")
                .hasAuthority("ADMIN");

        http.authorizeRequests()
                .antMatchers(
                        "/signup",
                        "/user/register",
                        "/registrationConfirm*",
                        "/badUser*",
                        "/forgotPassword*",
                        "/user/resetPassword*",
                        "/user/changePassword*",
                        "/user/savePassword*",
                        "/js/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .loginProcessingUrl("/doLogin")

                .and()
                    .logout().permitAll()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/doLogout", "GET"))

                .and()
                    .csrf().disable();

//        http
//                .rememberMe()
//                .tokenValiditySeconds(604800)  // a week
//                .key("MyAppKey")
////                .useSecureCookie(true);
//                .rememberMeCookieName("sticky-cookie")
//                .rememberMeParameter("remember");

        http
                .rememberMe()
                .tokenRepository(persistentTokenRepository());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

}
