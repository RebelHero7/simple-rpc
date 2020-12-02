package github.rebelhero.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请求错误的信息 枚举类
 *
 * @author rebelhero
 * @date 2020/11/24
 */
@Getter
@AllArgsConstructor
public enum RpcErrorEnum {
    /**
     *
     */
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    SERVICE_INVOCATION_FAIL("服务调用失败"),
    SERVICE_NOT_FOUND("没有找到指定的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务没有实现任何接口"),
    SERVICE_NOT_MATCH("返回结果错误！请求和返回的相应不匹配");

    /**
     * 错误信息
     */
    private final String message;
}
