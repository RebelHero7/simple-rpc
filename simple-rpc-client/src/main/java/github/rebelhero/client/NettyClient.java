package github.rebelhero.client;

import github.rebelhero.serializer.Serializer;
import github.rebelhero.serializer.kryo.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class NettyClient {

    private Serializer serializer;

    public NettyClient(Serializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 建立连接
     */
    public Channel connect(InetSocketAddress inetSocketAddress) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientInitializer(serializer));
            bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) result -> {
                if (result.isSuccess()) {
                    log.info("客户端连接成功！channel id为：{}", result.channel().id().asLongText());
                    completableFuture.complete(result.channel());
                } else {
                    eventLoopGroup.shutdownGracefully();
                    log.info("客户端连接失败！");
                }
            });
            return completableFuture.get();
        } catch (Exception e) {
            log.error("连接服务端时，发生异常", e);
        }
        return null;
    }

}
