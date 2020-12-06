package github.rebelhero.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import github.rebelhero.entity.RpcRequest;
import github.rebelhero.entity.RpcResponse;
import github.rebelhero.exception.SerializeException;
import github.rebelhero.extension.KoService;
import github.rebelhero.serializer.Serializer;

import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author rebelhero
 * @date 2020/11/24
 */
@KoService(name = "kryo")
public class KryoSerializer implements Serializer {

    /**
     * 由于kryo是线程不安全的，因此使用 ThreadLocal
     */
    private final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        //
        kryo.setRegistrationRequired(false);
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        return kryo;
    });

    /**
     * 序列化对象
     *
     * @param obj 要序列化的对象
     * @return 序列化好的字节数组
     */
    @Override
    public <T> byte[] serialize(T obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream);) {
            Kryo kryo = kryoLocal.get();

            kryo.writeObject(output, obj);
            kryoLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializeException("序列化失败!");
        }
    }

    /**
     * 反序列化
     *
     * @param bytes 要反序列化的字节数组
     * @param clazz 要反序列化的class
     * @return 反序列化后的对象
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoLocal.get();

            Object obj = kryo.readObject(input, clazz);
            return clazz.cast(obj);
        } catch (Exception e) {
            throw new SerializeException("反序列化失败！");
        }

    }
}
