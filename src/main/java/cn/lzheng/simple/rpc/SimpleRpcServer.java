package cn.lzheng.simple.rpc;


import cn.lzheng.rpc.enumeration.RegistryCode;
import cn.lzheng.rpc.serializer.CommonSerializer;
import cn.lzheng.rpc.transport.JDKSocket.server.SocketServer;
import cn.lzheng.rpc.transport.Netty.server.NettyServer;
import cn.lzheng.rpc.transport.RpcServer;
import cn.lzheng.simple.rpc.SimpleRpcProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;


/**
 * @ClassName SimpleRpcServer
 * @Author 6yi
 * @Date 2021/5/8 23:13
 * @Version 1.0
 * @Description: 已移入到AutoConfiguration中
 */


public class SimpleRpcServer implements ApplicationRunner{

    private Logger logger = LoggerFactory.getLogger(SimpleRpcServer.class);
    @Resource
    private SimpleRpcProperties config;

    public SimpleRpcServer(SimpleRpcProperties properties) {
        this.config = properties;
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
        rpcServer.start();
        logger.info("rpc服务启动..............");
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
