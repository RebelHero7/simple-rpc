package github.rebelhero.zk;

import java.net.InetSocketAddress;

/**
 * 服务注册
 *
 * @author rebelhero
 * @date 2020/11/26
 */
public interface ServiceRegistry {

    /**
     * 注册rpc服务。
     *
     * @param rpcServiceName    rpc服务名称
     * @param socketAddress 服务注册地址
     */
    void registerService(String rpcServiceName, InetSocketAddress socketAddress);
}
