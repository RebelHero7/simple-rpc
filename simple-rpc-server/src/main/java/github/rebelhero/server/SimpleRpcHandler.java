package github.rebelhero.server;

import cn.hutool.core.util.ObjectUtil;
import github.rebelhero.entity.RpcRequest;
import github.rebelhero.entity.RpcResponse;
import github.rebelhero.enums.RpcErrorEnum;
import github.rebelhero.enums.RpcResponseEnum;
import github.rebelhero.exception.RpcException;
import github.rebelhero.provider.ServiceProvider;
import github.rebelhero.provider.impl.ServiceProviderImpl;
import github.rebelhero.util.ThreadPoolFactoryUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;

/**
 * 只接受 RpcRequest 的服务。
 *
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class SimpleRpcHandler extends ChannelInboundHandlerAdapter {

    private static final String THREAD_NAME_PREFIX = "service-simple-rpc-pool";
    private final ExecutorService executorService;
    private ServiceProvider providerService = new ServiceProviderImpl();

    SimpleRpcHandler() {
        executorService = ThreadPoolFactoryUtils.getThreadPool(THREAD_NAME_PREFIX, false);
    }

    /**
     * 如果读取到。
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) {
        if (object instanceof RpcRequest) {
            RpcRequest rpcRequest = (RpcRequest) object;
            log.info("读取到请求, {}", rpcRequest);
            executorService.execute(() -> {
                Object serviceProvider = providerService.getProvider(rpcRequest.getInterfaceName());
                try {
                    Method method = serviceProvider.getClass()
                            .getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());

                    if (ObjectUtil.isNull(method)) {
                        log.error("方法找不到，方法名为：{}", rpcRequest.getMethodName());
                        throw new RpcException(RpcErrorEnum.SERVICE_NOT_FOUND);
                    }
                    Object obj = method.invoke(serviceProvider, rpcRequest.getParameters());
                    log.info("服务方法调用成功！服务为：{}, 方法为：{}， 结果为：{}", rpcRequest.getInterfaceName(),
                            rpcRequest.getMethodName(), obj);
                    RpcResponse response = RpcResponse.builder()
                            .code(RpcResponseEnum.SUCCESS.getCode())
                            .requestId(rpcRequest.getRequestId())
                            .data(obj)
                            .build();
                    channelHandlerContext.writeAndFlush(response);
                } catch (Exception e) {
                    log.error("处理数据时出错");
                    throw new RpcException(e);
                }
            });
        }

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("发生读空闲事件，关闭通道。");
                ctx.close();
            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("发生异常，关闭channel", cause);
        ctx.close();
    }
}
