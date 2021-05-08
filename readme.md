# simple-rpc-spring-boot-starter

此项目为simple-rpc对springBoot支持


## 食用说明

### 添加jar包
```pom
<dependency>
    <groupId>cn.lzheng</groupId>
    <artifactId>simple-rpc-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 启动类上使用注解开启Rpc服务扫描
@RpcServiceScans的value是扫描的包路径
```java
@SpringBootApplication
@RpcServiceScans("com.example.demo")
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 服务端实现接口
@RpcService标注为该类是rpc接口实现
```java
@RpcService
public class Hello implements HelloService {
    @Override
    public String hello(HelloObject helloObject) {
        return "hello";
    }
}
```

### 客户端服务调用
@RpcReference标注为是rpc接口
```java
@Controller
public class HelloController {
    @RpcReference
    HelloService service;
    
    @GetMapping("/hi")
    public String hi(){
        return service.hello(new HelloObject(1,""));
    }
}
```


### 配置文件参数
```properties
# 默认参数
simplerpc.host=127.0.0.1 //服务端的启动接口
simplerpc.port=-1       //服务端端口,-1时为随机
simplerpc.registryAddress=nacos://127.0.0.1:8848  //注册中心协议与地址，redis示范：redis://127.0.0.1:6379&PASS——WORD
simplerpc.transport=netty  //网络传输
simplerpc.serializer=kryo  //序列化
simplerpc.loadbalancer=random //负载均衡选择
```

### 部分配置参数可选内容

#### transport
- netty
- socket

#### serializer
- json
- kryo
- hession

#### loadbalancer
- random
- hash           
- roundrobin

