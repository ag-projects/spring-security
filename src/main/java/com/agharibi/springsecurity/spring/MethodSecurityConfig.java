package com.agharibi.springsecurity.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected RunAsManager runAsManager() {
        RunAsManagerImpl manager = new RunAsManagerImpl();
        manager.setKey("MyRunAsKey");
        return manager;
    }
}