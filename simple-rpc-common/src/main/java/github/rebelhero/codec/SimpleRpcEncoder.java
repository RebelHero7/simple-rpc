package github.rebelhero.codec;

import github.rebelhero.entity.RpcRequest;
import github.rebelhero.entity.RpcResponse;
import github.rebelhero.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rebelhero
 * @date 2020/11/25
 */
@Slf4j
public class SimpleRpcEncoder extends MessageToByteEncoder<Object> {

    private Class<?> genericClass;
    private Serializer serializer;

    public SimpleRpcEncoder(Class<?> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          Object obj, ByteBuf byteBuf) {
        if (genericClass.isInstance(obj)) {
            try {
                byte[] data = serializer.serialize(obj);
                byteBuf.writeInt(data.length);
                byteBuf.writeBytes(data);
            } catch (Exception ex) {
                log.error("encoder 时失败，错误原因", ex);
            }
        }
    }
}
