# my-spring-cloud

springboot + eureka + feign + zuul + hystrix + zipkin

## 项目启动
1.启动server-discovery

2.启动zipkin-server

3.启动server

4.启动client

5.启动gateway

6.浏览器访问注册中心 http://localhost:9000

7.浏览器访问zipkin http://localhost:7000

8.浏览器访问http://localhost:8888/client/helloWorld

## 从零开始my-spring-cloud
### 创建注册中心 server-discovery
1.创建springboot项目

2.pom文件导入eureka
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.zw</groupId>
    <artifactId>server-discovery</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>server-discovery</name>
    <description>server-discovery</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Greenwich.RC2</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
    </repositories>

</project>

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

### 远程服务接口 server-api
1.创建一个maven项目

2.修改pom文件

这个项目以jar包的形式引入到服务提供者以及服务消费者中，需要导入Feign,hystrix。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.zw</groupId>
    <artifactId>server-api</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>server-api</name>
    <description>server-api</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <skipTests>true</skipTests>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-source-plugin</artifactId>
                <configuration>
                    <attach>true</attach>
                </configuration>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>

```

3.创建远程方法调用接口HelloWorldService
```java
@FeignClient(name= "server", fallback = HelloWorldServiceFallBack.class)
public interface HelloWorldService {
    @GetMapping("helloWorld")
    String helloWorld();
}
```
@FeignClient注解中的name声明了服务提供者为"server"微服务，fallback则声明了一个接口的实现。
当服务体重这发生故障时，Hystrix会将fallback中的结果返回给服务调用者，实现了服务熔断，避免了故障在分布式系统中的蔓延。

4.创建HelloWorldService的fallback实现类
```java
@Component
public class HelloWorldServiceFallBack implements HelloWorldService {
    @Override
    public String helloWorld() {
        return "hello world fall back";
    }
}
```