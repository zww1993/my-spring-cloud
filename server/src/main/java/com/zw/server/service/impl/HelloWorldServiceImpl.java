package com.zw.server.service.impl;

import com.zw.server.api.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HelloWorldServiceImpl implements HelloWorldService {

    @Override
    public String helloWorld() {
        return "hello world";
    }
}
