package com.agharibi.springsecurity.spring;

import com.agharibi.springsecurity.model.User;
import com.agharibi.springsecurity.persistence.UserRepository;
import com.agharibi.springsecurity.security.CustomAuthenticationProvider;
import com.agharibi.springsecurity.security.LoggingFilter;
import com.agharibi.springsecurity.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@EnableWebSecurity
@EnableAsync
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private LoggingFilter loggingFilter;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig() {
        super();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("pass").roles("USER")
//                .and()
//                .withUser("admin").password("pass").roles("ADMIN");

//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .withUser("user").password(encoder().encode("Secured123!")).roles("USER")
//                .and()
//                .withUser("admin").password(encoder().encode("Secured123!")).roles("ADMIN");

         auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // adding custom filter to the chain of spring security existing filters
        http.addFilterBefore(loggingFilter, AnonymousAuthenticationFilter.class);

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
                    .sessionManagement().maximumSessions(1)
                    .sessionRegistry(sessionRegistry()).and().sessionFixation().none()

                .and()
                    .csrf().disable();
//
//        http
//                .rememberMe()
//                .tokenRepository(persistentTokenRepository());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider runAsAuthenticationProvider() {
        RunAsImplAuthenticationProvider authenticationProvider = new RunAsImplAuthenticationProvider();
        authenticationProvider.setKey("MyRunAsKey");
        return authenticationProvider;
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(encoder());
        return daoAuthenticationProvider;
    }

    @PostConstruct
    private void saveTestUser() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword(encoder().encode("Secured123!"));
        user.setEnabled(true);

        userRepository.save(user);
    }

    @PreDestroy
    private void deleteTestUser() {
        userRepository.deleteAll();
    }

}
