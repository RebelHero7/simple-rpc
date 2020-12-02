package github.rebelhero.server;

import github.rebelhero.provider.ServiceProvider;
import github.rebelhero.provider.impl.ServiceProviderImpl;
import github.rebelhero.serializer.Serializer;
import github.rebelhero.util.ThreadPoolFactoryUtils;
import github.rebelhero.zk.util.ZkCuratorUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * netty 启动类，同时将服务注册到zk上。
 *
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class SimpleRpcServer {

    /**
     * 服务提供端口
     */
    private int port;

    /**
     * 序列化方式
     */
    private Serializer serializer;

    private ServiceProvider serviceProvider;

    private InetSocketAddress inetSocketAddress;

    public SimpleRpcServer(int port, Serializer serializer) {
        this.port = port;
        this.serializer = serializer;
        serviceProvider = new ServiceProviderImpl();
        this.inetSocketAddress = new InetSocketAddress("127.0.0.1", port);
    }

    public <T> void registry(T service, Class<T> serviceClass) {

        String serviceName = serviceClass.getCanonicalName();
        ZkCuratorUtils.createPersistentNode(serviceName, inetSocketAddress);
        serviceProvider.addProvider(serviceName, service);
        startService();
    }


    private void startService() {
        // 埋一个钩子方法，在jvm 关闭前会将其关闭。
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ZkCuratorUtils.clearRegistry(inetSocketAddress);
            ThreadPoolFactoryUtils.shutDownAllThreadPool();
        }));


        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new SimpleChannelInitializer(serializer));
            ChannelFuture cf = serverBootstrap.bind("127.0.0.1", port).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动netty 时发生异常", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
