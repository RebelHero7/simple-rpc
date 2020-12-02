package github.rebelhero.client;

import github.rebelhero.codec.SimpleRpcDecoder;
import github.rebelhero.codec.SimpleRpcEncoder;
import github.rebelhero.entity.RpcRequest;
import github.rebelhero.entity.RpcResponse;
import github.rebelhero.handler.ClientHandler;
import github.rebelhero.serializer.Serializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private Serializer serializer;

    ClientInitializer(Serializer serializer) {
        this.serializer = serializer;
    }


    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new SimpleRpcEncoder(RpcRequest.class, serializer));
        pipeline.addLast(new SimpleRpcDecoder(RpcResponse.class, serializer));
        pipeline.addLast(new ClientHandler());
    }
}
