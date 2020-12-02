package github.rebelhero.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rebelhero
 * @date 2020/11/24
 */
@Getter
@AllArgsConstructor
public enum RpcResponseEnum {

    /**
     * 请求正确
     */
    SUCCESS(200, "请求成功！"),

    /**
     * 请求失败
     */
    FAIL(500, "请求失败！");

    /**
     * 错误编码
     */
    private final int code;

    /**
     * 错误信息
     */
    private final String message;


}
