package com.agharibi.springsecurity.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncBean {

    @Async
    public void asyncCall() {
        System.out.println();
    }
}
