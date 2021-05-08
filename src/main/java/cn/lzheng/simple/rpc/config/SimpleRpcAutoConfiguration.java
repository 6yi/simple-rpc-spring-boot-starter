package cn.lzheng.simple.rpc.config;

import cn.lzheng.rpc.enumeration.RegistryCode;
import cn.lzheng.rpc.loadbalancer.LoadBalancer;
import cn.lzheng.rpc.spring.SpringBeanPostProcessor;
import cn.lzheng.rpc.transport.JDKSocket.client.SocketClient;
import cn.lzheng.rpc.transport.JDKSocket.server.SocketServer;
import cn.lzheng.rpc.transport.Netty.client.NettyClient;
import cn.lzheng.rpc.transport.Netty.server.NettyServer;
import cn.lzheng.rpc.transport.RpcClient;
import cn.lzheng.rpc.transport.RpcClientProxy;
import cn.lzheng.rpc.transport.RpcServer;
import cn.lzheng.simple.rpc.CommonUtils;
import cn.lzheng.simple.rpc.SimpleRpcProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;


/**
 * @ClassName SimpleRpcAutoConfiguration
 * @Author 6yi
 * @Date 2021/5/8 20:58
 * @Version 1.0
 * @Description:
 */

@Configuration
@EnableConfigurationProperties(SimpleRpcProperties.class)
@ConditionalOnProperty(prefix = "simple.rpc",value = "enabled",matchIfMissing = true)
public class SimpleRpcAutoConfiguration implements ApplicationRunner {

    @Resource
    private SimpleRpcProperties config;

    private Logger logger = LoggerFactory.getLogger(SimpleRpcAutoConfiguration.class);

    //添加AOP
    @Bean
    public SpringBeanPostProcessor springBeanPostProcessor(){
        return new SpringBeanPostProcessor(new RpcClientProxy(rpcClient()));
    }


    public RpcClient rpcClient(){
        String address = config.getRegistryAddress().toLowerCase();
        String[] registryAndURI = address.split("://");
        if("nacos".equals(registryAndURI[0])){
            return createRpcClient(registryAndURI[1],
                    RegistryCode.NACOS.getCode(),
                    config.getTransport(),
                    config.getSerializer(),
                    config.getLoadbalancer());
        }else if("redis".equals(registryAndURI[0])){
            return createRpcClient(registryAndURI[1],
                    RegistryCode.REDIS.getCode(),
                    config.getTransport(),
                    config.getSerializer(),
                    config.getLoadbalancer());
        }
        return null;
    }

    public RpcClient createRpcClient(String URI,Integer discovery,String transport ,String serialize, String loadBalancer){
        Integer serializer = CommonUtils.getSerializer(serialize);
        LoadBalancer balancer = CommonUtils.getLoadBalancer(loadBalancer);
        if("netty".equals(transport)){
            return new NettyClient(URI,discovery,serializer,balancer);
        }else if("socket".equals(transport)){
            return new SocketClient(URI,discovery,serializer,balancer);
        }
        return null;
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
        String Host = null;
        if(config.getHost()==null){
            Host = "127.0.0.1";
        }else{
            Host = config.getHost();
        }
        String transport = config.getTransport();
        Integer port = config.getPort()==-1?null: config.getPort();
        String address = config.getRegistryAddress().toLowerCase();
        String[] registryAndURI = address.split("://");

        RpcServer rpcServer = null;
        if("nacos".equals(registryAndURI[0])){
            rpcServer = createRpcServer(Host,
                    port,
                    registryAndURI[1],
                    RegistryCode.NACOS.getCode(),
                    transport,
                    config.getSerializer());
        }else if("redis".equals(registryAndURI[1])){
            rpcServer = createRpcServer(Host,
                    port ,
                    registryAndURI[1],
                    RegistryCode.REDIS.getCode(),
                    transport, config.getSerializer());
        }
        logger.info("rpc服务启动中..............");
        rpcServer.start();
    }

    public RpcServer createRpcServer(String Host,Integer port,
                                     String URI,Integer discovery,
                                     String transport ,String serialize){
        if("netty".equals(transport)){
            return new NettyServer(Host,
                    port,
                    CommonUtils.getSerializer(serialize),
                    discovery,
                    URI);
        }else if("socket".equals(transport)){
            return new SocketServer(Host,
                    port,
                    CommonUtils.getSerializer(serialize),
                    discovery,
                    URI);
        }
        return null;
    }


}
