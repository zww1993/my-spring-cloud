package com.zw.client.controller;

import com.zw.server.api.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class HelloWorldController {

    @Resource
    private HelloWorldService helloWorldService;

    @GetMapping("helloWorld")
    public String helloWorld() {
        return helloWorldService.helloWorld();
    }

}
