package github.rebelhero.codec;

import github.rebelhero.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class SimpleRpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;
    private Serializer serializer;

    public SimpleRpcDecoder(Class<?> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    /**
     * 解码
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int readableBytes = in.readableBytes();

        if (readableBytes < 4) {
            return;
        }
        // 记录当前位置，后期如果可读字节数与实际不等可重置。
        in.markReaderIndex();
        int dataLength = in.readInt();
        // 不等，则重置
        if (readableBytes < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        try {
            // 反序列化类。
            Object obj = serializer.deserialize(data, genericClass);
            out.add(obj);
        } catch (Exception ex) {
            log.error("解码时发生错误：", ex);
        }
    }


}
