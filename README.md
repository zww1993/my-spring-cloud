# my-spring-cloud

springboot + eureka + feign + zuul + Ribbon + hystrix + zipkin

## 目录
* [项目启动]
* [开始搭建my-spring-cloud]
    + [注册中心server-discovery]
    + [远程服务接口server-api]
    + [服务提供者server]
    + [服务调用者client]

[项目启动]: #项目启动
[开始搭建my-spring-cloud]: #开始搭建my-spring-cloud
[注册中心server-discovery]: #注册中心server-discovery
[远程服务接口server-api]: #远程服务接口server-api
[服务提供者server]: #服务提供者server
[服务调用者client]: #服务调用者client

## 项目启动
1.启动server-discovery

2.启动zipkin-server

3.启动server

4.启动client

5.启动gateway

6.浏览器访问注册中心 http://localhost:9000

7.浏览器访问zipkin http://localhost:7000

8.浏览器访问http://localhost:8888/client/helloWorld

## 开始搭建my-spring-cloud
### 注册中心server-discovery
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

### 远程服务接口server-api
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

### 服务提供者server
1.创建一个springboot项目

2.修改pom文件引入eureka、zipkin、log4j2及相关组件，并且引入刚才创建的server-api包
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
    <artifactId>server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>server</name>
    <description>server</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Greenwich.RC2</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
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

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
        </dependency>

        <dependency>
            <groupId>com.zw</groupId>
            <artifactId>server-api</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <!--log4j-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>

        <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-yaml</artifactId>
            <version>2.9.3</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
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
    name: server
  zipkin:
    base-url: http://localhost:7000
  sleuth:
    sampler:
      percentage: 1.0
server:
  port: 8000
eureka:
  client:
    service-url:
      defaultZone: http://localhost:9000/eureka
```

4.启动类添加@EnableEurekaClient注解注册到注册中心
```java
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.zw.server"})
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
```
5.创建HelloWorldService实现类
```java
@RestController
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String helloWorld() {
        return "hello world";
    }
}
```

### 服务调用者client
1.创建一个springboot项目

2.修改pom文件引入eureka、zipkin、log4j2及相关组件，并且引入刚才创建的server-api包
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
	<artifactId>client</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>client</name>
	<description>client</description>

	<properties>
		<java.version>1.8</java.version>
		<spring-cloud.version>Greenwich.RC2</spring-cloud.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
			<exclusions>
				<exclusion>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-zipkin</artifactId>
		</dependency>

		<dependency>
			<groupId>com.zw</groupId>
			<artifactId>server-api</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>

		<!--log4j-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-log4j2</artifactId>
		</dependency>

		<dependency>
			<groupId>com.fasterxml.jackson.dataformat</groupId>
			<artifactId>jackson-dataformat-yaml</artifactId>
			<version>2.9.3</version>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
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
    name: client
  zipkin:
    base-url: http://localhost:7000
  sleuth:
    sampler:
      percentage: 1.0
server:
  port: 8001
eureka:
  client:
    service-url:
      defaultZone: http://localhost:9000/eureka
feign:
  hystrix:
    enabled: true
```

4.修改启动类

添加@EnableDiscoveryClient注解也可以将服务注册到注册中心。@EnableCircuitBreaker将会开启hystrix断路器功能。

```java
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.zw.server.api"})
@EnableCircuitBreaker
@SpringBootApplication(scanBasePackages = {"com.zw.server.api", "com.zw.client"})
public class ClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
}
```

5.创建HelloWorldController

```java
@RestController
public class HelloWorldController {

    @Resource
    private HelloWorldService helloWorldService;

    @GetMapping("helloWorld")
    public String helloWorld() {
        return helloWorldService.helloWorld();
    }

}
```

##### 到这里就实现了服务治理以及服务调用，依次启动注册中心server-discovery、服务提供者server、服务调用者client，然后使用浏览器调用浏览器访问http://localhost:8001/helloWorld即可。
