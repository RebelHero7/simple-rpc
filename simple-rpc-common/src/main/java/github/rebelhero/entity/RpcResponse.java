package github.rebelhero.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author rebelhero
 * @date 2020/11/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 1364591404480325753L;

    /**
     * 请求id， 表示回应的是哪条请求。
     */
    private String requestId;

    /**
     * 请求码。
     */
    private Integer code;

    /**
     * 请求message。
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

}
