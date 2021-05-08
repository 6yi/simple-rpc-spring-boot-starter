package cn.lzheng.simple.rpc;

import cn.lzheng.rpc.loadbalancer.HashLoadBalancer;
import cn.lzheng.rpc.loadbalancer.LoadBalancer;
import cn.lzheng.rpc.loadbalancer.RandomLoadBalancer;
import cn.lzheng.rpc.loadbalancer.RoundRobinLoadBalancer;
import cn.lzheng.rpc.serializer.CommonSerializer;

/**
 * @ClassName CommonUtils
 * @Author 6yi
 * @Date 2021/5/8 23:27
 * @Version 1.0
 * @Description:
 */


public class CommonUtils {
    public static LoadBalancer getLoadBalancer(String s){
        if("random".equals(s))
            return new RandomLoadBalancer();
        if("hash".equals(s))
            return new HashLoadBalancer();
        if("roundrobin".equals(s))
            return new RoundRobinLoadBalancer();
        return null;
    }


    public static Integer getSerializer(String s){
        if("kryo".equals(s))
            return CommonSerializer.KRYO_SERIALIZER;
        if("json".equals(s))
            return CommonSerializer.JSON_SERIALIZER;
        if("hession".equals(s))
            return CommonSerializer.HESSIAN_SERIALIZER;
        return null;
    }

}
