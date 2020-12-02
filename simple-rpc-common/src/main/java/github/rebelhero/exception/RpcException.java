package github.rebelhero.exception;

import github.rebelhero.enums.RpcErrorEnum;

/**
 * rpc 调用时出现的异常。
 *
 * @author rebelhero
 * @date 2020/11/24
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcErrorEnum rpcErrorEnum) {
        super(rpcErrorEnum.getMessage());
    }

    public RpcException(Throwable throwable) {
        super(throwable);
    }
}
