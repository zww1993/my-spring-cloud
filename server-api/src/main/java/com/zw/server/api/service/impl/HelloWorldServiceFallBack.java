package com.zw.server.api.service.impl;

import com.zw.server.api.service.HelloWorldService;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldServiceFallBack implements HelloWorldService {

    @Override
    public String helloWorld() {
        return "hello world fall back";
    }
}
