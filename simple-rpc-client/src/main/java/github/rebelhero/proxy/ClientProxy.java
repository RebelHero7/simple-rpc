package github.rebelhero.proxy;

import github.rebelhero.entity.RpcRequest;
import github.rebelhero.entity.RpcResponse;
import github.rebelhero.enums.RpcErrorEnum;
import github.rebelhero.enums.RpcResponseEnum;
import github.rebelhero.exception.RpcException;
import github.rebelhero.serializer.Serializer;
import github.rebelhero.transport.MessageTransport;
import github.rebelhero.transport.impl.MessageTransportImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class ClientProxy implements InvocationHandler {

    private MessageTransport messageTransport;

    public ClientProxy(Serializer serializer) {
        messageTransport = new MessageTransportImpl(serializer);
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("invoked method: [{}]", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .parameterTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .build();
        CompletableFuture<RpcResponse> completableFuture = messageTransport.sendMessage(rpcRequest);
        RpcResponse rpcResponse = completableFuture.get();
        if (rpcResponse == null ||
                rpcResponse.getCode() == null ||
                !rpcResponse.getCode().equals(RpcResponseEnum.SUCCESS.getCode())) {
            log.error(RpcErrorEnum.SERVICE_INVOCATION_FAIL.getMessage());
            throw new RpcException(RpcErrorEnum.SERVICE_INVOCATION_FAIL);
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorEnum.SERVICE_NOT_MATCH);
        }

        return rpcResponse;
    }
}
