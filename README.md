# my-spring-cloud

springboot + eureka + feign + zuul + hystrix + zipkin

1.启动server-discovery

2.启动zipkin-server

3.启动server

4.启动client

5.启动gateway

6.浏览器访问注册中心 http://localhost:9000

7.浏览器访问zipkin http://localhost:7000

8.浏览器访问http://localhost:8888/client/helloWorld

# 从零开始my-spring-cloud
## 注册中心

1.创建一个springboot项目

2.pom文件导入eureka
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

3.添加application.yml配置文件
```yaml
spring:
  application:
    name: server-discovery
server:
  port: 9000
eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

4.启动类添加注解
```java
@EnableEurekaServer
@SpringBootApplication
public class ServerDiscoveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerDiscoveryApplication.class, args);
    }
}
```

5.启动项目

6.浏览器访问http://localhost:9000
