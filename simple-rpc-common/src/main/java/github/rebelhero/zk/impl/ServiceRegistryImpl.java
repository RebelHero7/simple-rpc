package github.rebelhero.zk.impl;

import github.rebelhero.zk.util.ZkCuratorUtils;
import github.rebelhero.zk.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author rebelhero
 * @date 2020/11/26
 */
@Slf4j
public class ServiceRegistryImpl implements ServiceRegistry {
    /**
     * 注册rpc服务。
     *
     * @param rpcServiceName rpc服务名称
     * @param socketAddress  服务注册地址
     */
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress socketAddress) {
        ZkCuratorUtils.createPersistentNode(rpcServiceName, socketAddress);
    }
}
