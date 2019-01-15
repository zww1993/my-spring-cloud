package com.zw.server.service.impl;

import com.zw.server.api.service.HelloWorldService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldServiceImpl implements HelloWorldService {

    @Override
    public String helloWorld() {
        return "hello world";
    }
}
