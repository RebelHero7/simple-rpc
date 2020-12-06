package github.rebelhero.loadbalance;

import cn.hutool.core.collection.CollUtil;

import java.util.List;

/**
 * @author rebelhero
 * @date 2020/12/3
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    /**
     * 根据负载均衡算法选择节点。
     *
     * @param childrenNodes 所有子节点
     * @return 选择的节点。
     * @date 2020/12/2
     */
    @Override
    public String choose(List<String> childrenNodes) {
        if (CollUtil.isEmpty(childrenNodes)) {
            return null;
        }
        // 如果 invokers 列表中仅有一个 Invoker，直接返回即可，无需进行负载均衡
        if (childrenNodes.size() == 1) {
            return childrenNodes.get(0);
        }

        // 调用 doSelect 方法进行负载均衡，该方法为抽象方法，由子类实现
        return doChoose(childrenNodes);
    }

    /**
     * 注解见上
     *
     * @param childrenNodes 所有子节点
     * @return 选择的节点。
     */
    protected abstract String doChoose(List<String> childrenNodes);
}
