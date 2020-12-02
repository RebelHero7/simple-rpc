package github.rebelhero.exception;

/**
 * 序列化时的异常
 *
 * @author rebelhero
 * @date 2020/11/24
 */
public class SerializeException extends RuntimeException {

    public SerializeException(String message) {
        super(message);
    }
}
