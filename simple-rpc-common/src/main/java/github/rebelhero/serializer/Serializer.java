package github.rebelhero.serializer;

import github.rebelhero.extension.KO;

/**
 * @author rebelhero7
 * @date 2020/11/24
 */
@KO
public interface Serializer {

    /**
     * 序列化对象
     *
     * @param obj 要序列化的对象
     * @return 序列化好的字节数组
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     *
     * @param bytes 要反序列化的字节数组
     * @param clazz 要反序列化的class
     * @return 反序列化后的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
