package com.agharibi.springsecurity.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected RunAsManager runAsManager() {
        RunAsManagerImpl manager = new RunAsManagerImpl();
        manager.setKey("MyRunAsKey");
        return manager;
    }

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        Map<String, List<ConfigAttribute>> methodMap = new HashMap<>();
        String method = "com.agharibi.springsecurity.web.controller.UserController.createForm*";

        // createFrom method from UserController class requires  ROLE_ADMIN
        methodMap.put(method, SecurityConfig.createList("ROLE_ADMIN"));

        return new MapBasedMethodSecurityMetadataSource(methodMap);
    }
}
