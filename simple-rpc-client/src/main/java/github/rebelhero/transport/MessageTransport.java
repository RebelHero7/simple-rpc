package github.rebelhero.transport;

import github.rebelhero.entity.RpcRequest;
import github.rebelhero.entity.RpcResponse;
import github.rebelhero.loadbalance.LoadBalance;

import java.util.concurrent.CompletableFuture;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
public interface MessageTransport {

    /**
     * 发送请求。
     *
     * @param rpcRequest  数据请求
     * @param loadBalance 负载均衡。
     * @return CompletableFuture<RpcResponse>
     * @date 2020/11/30
     */
    CompletableFuture<RpcResponse> sendMessage(RpcRequest rpcRequest, LoadBalance loadBalance);

}
