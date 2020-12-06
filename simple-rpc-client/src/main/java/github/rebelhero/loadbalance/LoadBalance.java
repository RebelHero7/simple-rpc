package github.rebelhero.loadbalance;

import java.util.List;

/**
 * 负载均衡 接口
 *
 * @author rebelhero
 * @date 2020/12/2
 */
public interface LoadBalance {

    /**
     * 根据负载均衡算法选择节点。
     *
     * @param childrenNodes 所有子节点
     * @return 选择的节点。
     * @date 2020/12/2
     */
    String choose(List<String> childrenNodes);

}
