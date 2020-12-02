package github.rebelhero.provider;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
public interface ServiceProvider {

    /**
     * 新增服务提供。
     *
     * @param serviceName 服务的名称
     * @param service     具体提供服务的实例。
     * @date 2020/11/30
     */
    <T> void addProvider(String serviceName, T service);


    /**
     * 获取服务实例对象
     *
     * @param serviceName 服务名称
     * @return 获取其服务的名称的实体。
     * @date 2020/11/30
     */
    Object getProvider(String serviceName);


    /**
     * 清除服务提供者
     *
     * @param serviceName 服务的名称
     */
    void clearProviderByName(String serviceName);

}
