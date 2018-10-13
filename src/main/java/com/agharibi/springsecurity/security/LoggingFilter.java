package com.agharibi.springsecurity.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class LoggingFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String URL = httpServletRequest.getRequestURL().toString();
        String queryString = Optional.ofNullable(httpServletRequest.getQueryString()).map(value -> "?" + value).orElse("");
        logger.info(String.format("applying LoggingFilter for URI: %s%s", URL, queryString));

        filterChain.doFilter(request, response);
    }
}
