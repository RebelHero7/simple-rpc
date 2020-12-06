package github.rebelhero.loadbalance.impl;

import github.rebelhero.loadbalance.AbstractLoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡。
 *
 * @author rebelhero
 * @date 2020/12/2
 */
@Slf4j
public class RandomLoadBalance extends AbstractLoadBalance {

    private final Random random = new Random();

    @Override
    protected String doChoose(List<String> childrenNodes) {
        int length = childrenNodes.size();
        int offset = random.nextInt(length);
        log.info("选中的节点index为：{}", offset);
        return childrenNodes.get(offset);
    }
}
