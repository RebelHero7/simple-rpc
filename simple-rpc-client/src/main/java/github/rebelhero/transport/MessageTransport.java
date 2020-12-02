package github.rebelhero.transport;

import cn.hutool.core.util.ObjectUtil;
import github.rebelhero.entity.RpcRequest;
import github.rebelhero.entity.RpcResponse;

import java.util.concurrent.CompletableFuture;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
public interface MessageTransport {

    /**
     * 发送请求。
     *
     * @param rpcRequest 数据请求
     * @return CompletableFuture<RpcResponse>
     * @date 2020/11/30
     */
    CompletableFuture<RpcResponse> sendMessage(RpcRequest rpcRequest);

}
