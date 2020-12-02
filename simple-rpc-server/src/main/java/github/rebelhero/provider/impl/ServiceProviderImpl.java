package github.rebelhero.provider.impl;

import cn.hutool.core.util.ObjectUtil;
import github.rebelhero.enums.RpcErrorEnum;
import github.rebelhero.exception.RpcException;
import github.rebelhero.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {
    /**
     * key -> 服务名，value -> 提供服务的实体。
     */
    private static Map<String, Object> serviceMap = new ConcurrentHashMap<>();


    /**
     * 新增服务提供。
     *
     * @param serviceName 服务的名称
     * @param service     具体提供服务的实例。
     * @date 2020/11/30
     */
    @Override
    public <T> void addProvider(String serviceName, T service) {
        if (ObjectUtil.isNull(serviceMap.get(serviceName))) {
            serviceMap.put(serviceName, service);
            log.info("新增服务提供者，serviceName为：{}", serviceName);
        } else {
            log.info("{}, 服务已存在", serviceName);
        }
    }

    /**
     * 获取服务实例对象
     *
     * @param serviceName 服务名称
     * @return 获取其服务的名称的实体。
     * @date 2020/11/30
     */
    @Override
    public Object getProvider(String serviceName) {
        Object object = serviceMap.get(serviceName);
        if (ObjectUtil.isNull(object)) {
            throw new RpcException(RpcErrorEnum.SERVICE_NOT_FOUND);
        }
        return object;
    }

    /**
     * 清除服务提供者
     *
     * @param serviceName 服务的名称
     */
    @Override
    public void clearProviderByName(String serviceName) {
        serviceMap.remove(serviceName);
    }
}
