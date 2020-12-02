package github.rebelhero.zk.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.listen.StandardListenerManager;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * zk Curator 工具类
 *
 * @author rebelhero
 * @date 2020/11/26
 */
@Slf4j
public class ZkCuratorUtils {

    /**
     * 建立连接时重试的时间，单位，毫秒。
     */
    private static final int BASE_SLEEP_TIME = 1000;
    /**
     * 建立连接时重试的次数。
     */
    private static final int MAX_RETRIES = 3;
    private static final String ZK_REGISTER_ROOT_PATH = "/simple-rpc/";
    /**
     * 提供rpc服务的节点。
     */
    private static final Map<String, List<String>> PROVIDE_SERVICE_ADDRESS = new ConcurrentHashMap<>();

    /**
     * 已注册的服务地址set
     */
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static CuratorFramework zkClient;
    private static final String ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

    private ZkCuratorUtils() {
    }

    /**
     * 创建持久化节点。
     *
     * @param rpcServiceName rpc 服务名称
     * @param socketAddress  地址
     */
    public static void createPersistentNode(String rpcServiceName, InetSocketAddress socketAddress) {
        String path = ZK_REGISTER_ROOT_PATH + rpcServiceName + socketAddress;
        log.info("要获取的节点路径为:{}", path);
        CuratorFramework zkClient = ZkCuratorUtils.getZkClient();
        try {
            if (!REGISTERED_PATH_SET.contains(path)) {
                // 同时zk上也没有，那就创建一个。
                if (ObjectUtil.isNull(zkClient.checkExists().forPath(path))) {
                    zkClient.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.PERSISTENT)
                            .forPath(path);
                    log.info("创建zk持久化节点成功，地址为：{}", path);
                }
                REGISTERED_PATH_SET.add(path);
            }
        } catch (Exception e) {
            log.error("创建持久化节点失败！原因为：{}", e.getMessage());
        }
    }

    /**
     * 获取子节点地址。
     *
     * @param rpcServiceName rpc serviceName
     */
    public static List<String> getChildrenNodes(String rpcServiceName) {
        if (PROVIDE_SERVICE_ADDRESS.containsKey(rpcServiceName)) {
            return PROVIDE_SERVICE_ADDRESS.get(rpcServiceName);
        }
        List<String> result = null;
        String servicePath = ZK_REGISTER_ROOT_PATH + rpcServiceName;
        try {
            CuratorFramework zkClient = getZkClient();
            result = zkClient.getChildren().forPath(servicePath);
            PROVIDE_SERVICE_ADDRESS.put(rpcServiceName, result);
            registerWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            log.error("获取其子节点：{} 失败！", servicePath);
        }
        return result;
    }

    /**
     * 清除监听。
     */
    public static void clearRegistry(InetSocketAddress inetSocketAddress) {
        CuratorFramework zkClient = getZkClient();
        REGISTERED_PATH_SET.forEach(registeredPath -> {
            if (registeredPath.endsWith(inetSocketAddress.toString())) {
                try {
                    zkClient.delete().forPath(registeredPath);
                } catch (Exception ex) {
                    log.error("清除路径出错，原因为：{}", ex.getMessage());
                }
            }
        });
        log.info("已清除所有注册路径！");
    }

    /**
     * 注册监听
     *
     * @param rpcServiceName rpc服务名称
     */
    private static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) throws Exception {
        String servicePath = ZK_REGISTER_ROOT_PATH + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (client, event) -> {
            List<String> serviceAddresses = client.getChildren().forPath(servicePath);
            PROVIDE_SERVICE_ADDRESS.put(rpcServiceName, serviceAddresses);
        };
        StandardListenerManager<PathChildrenCacheListener> standardListenerManager =
                StandardListenerManager.standard();
        standardListenerManager.addListener(pathChildrenCacheListener);
//        pathChildrenCache.getListenable().addListener(standardListenerManager);
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
    }

    /**
     * 获得zk client
     */
    private static CuratorFramework getZkClient() {
        if (ObjectUtil.isNotNull(zkClient) && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(ZOOKEEPER_ADDRESS)
                .retryPolicy(new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES))
                .build();
        zkClient.start();
        return zkClient;
    }
}
