package com.agharibi.springsecurity.spring;

import com.zaxxer.hikari.HikariDataSource;
import net.sf.ehcache.CacheManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private static final String SELECT_IDENTITY = "SELECT @@IDENTITY";

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

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator());
        return expressionHandler;
    }

    @Bean
    public AclPermissionEvaluator aclPermissionEvaluator() {
        return new AclPermissionEvaluator(aclService());
    }

    @Bean
    public JdbcMutableAclService aclService() {
        JdbcMutableAclService service = new JdbcMutableAclService(dataSource(), lookupStrategy(), aclCache());
        service.setClassIdentityQuery(SELECT_IDENTITY);
        service.setSidIdentityQuery(SELECT_IDENTITY);

        return service;
    }

//    @Primary
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }

    @Primary
    @Bean
    @ConfigurationProperties("app.datasource")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public AclCache aclCache() {
        EhCacheManagerFactoryBean cacheManager = new EhCacheManagerFactoryBean();
        cacheManager.setAcceptExisting(true);
        cacheManager.setCacheManagerName(CacheManager.getInstance().getName());
        cacheManager.afterPropertiesSet();

        EhCacheFactoryBean factoryBean = new EhCacheFactoryBean();
        factoryBean.setName("aclCache");

        factoryBean.setCacheManager(cacheManager.getObject());
        factoryBean.setMaxBytesLocalHeap("16M");
        factoryBean.setMaxEntriesLocalHeap(0L);
        factoryBean.afterPropertiesSet();

        return new EhCacheBasedAclCache(factoryBean.getObject(), permissionGrantingStrategy(), aclAuthorizationStrategy());
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(dataSource(), aclCache(), aclAuthorizationStrategy(), permissionGrantingStrategy());
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ADMIN"));
    }

}
