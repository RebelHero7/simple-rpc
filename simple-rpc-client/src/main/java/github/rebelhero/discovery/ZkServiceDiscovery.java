package github.rebelhero.discovery;

import github.rebelhero.loadbalance.LoadBalance;
import github.rebelhero.zk.util.ZkCuratorUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class ZkServiceDiscovery {

    public InetSocketAddress lookupService(String serviceName, LoadBalance loadBalance) {
        List<String> childrenNodes = ZkCuratorUtils.getChildrenNodes(serviceName);
        String serviceAddress = loadBalance.choose(childrenNodes);
        log.info("找到服务地址为：{}", serviceAddress);
        String[] socketAddress = serviceAddress.split(":");
        String ip = socketAddress[0];
        int port = Integer.parseInt(socketAddress[1]);
        return new InetSocketAddress(ip, port);
    }
}
