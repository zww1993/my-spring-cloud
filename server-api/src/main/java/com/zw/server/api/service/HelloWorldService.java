package com.zw.server.api.service;

import com.zw.server.api.service.impl.HelloWorldServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name= "server", fallback = HelloWorldServiceFallBack.class)
public interface HelloWorldService {

    @GetMapping("helloWorld")
    String helloWorld();

}
