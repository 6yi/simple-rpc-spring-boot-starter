package cn.lzheng.simple.rpc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName SimpleRpcProperties
 * @Author 6yi
 * @Date 2021/5/9 0:30
 * @Version 1.0
 * @Description:
 */

@Component
@ConfigurationProperties(
        prefix = "simplerpc"
)
public class SimpleRpcProperties {
    private String registryAddress = "nacos://127.0.0.1:8848";
    private String host;
    private int port = -1;
    private String serializer = "kryo";
    private String transport = "netty";
    private String loadbalancer = "random";

    public SimpleRpcProperties() {
    }

    public String getLoadbalancer() {
        return this.loadbalancer;
    }

    public void setLoadbalancer(String loadbalancer) {
        this.loadbalancer = loadbalancer;
    }

    public String getTransport() {
        return this.transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getRegistryAddress() {
        return this.registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSerializer() {
        return this.serializer;
    }

    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }
}

