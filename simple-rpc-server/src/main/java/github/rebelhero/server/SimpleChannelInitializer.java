package github.rebelhero.server;

import github.rebelhero.codec.SimpleRpcDecoder;
import github.rebelhero.codec.SimpleRpcEncoder;
import github.rebelhero.entity.RpcRequest;
import github.rebelhero.entity.RpcResponse;
import github.rebelhero.serializer.Serializer;
import github.rebelhero.serializer.kryo.KryoSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
public class SimpleChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Serializer serializer;

    SimpleChannelInitializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 90秒没有连接就关闭。
        pipeline.addLast(new IdleStateHandler(90, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        pipeline.addLast(new SimpleRpcDecoder(RpcRequest.class, serializer));
        pipeline.addLast(new SimpleRpcEncoder(RpcResponse.class, serializer));
        pipeline.addLast(new SimpleRpcHandler());
    }
}
