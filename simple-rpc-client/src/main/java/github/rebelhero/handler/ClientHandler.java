package github.rebelhero.handler;

import github.rebelhero.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("客户端channel ID为:{}", ctx.channel().id().asLongText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        log.info("客户端接收到消息为为：{}", rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("channel发生异常", cause);
        ctx.close();
    }
}
