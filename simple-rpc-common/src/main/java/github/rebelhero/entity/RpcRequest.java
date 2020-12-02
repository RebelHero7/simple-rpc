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
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 5561235000120187897L;
    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 请求的接口名。
     */
    private String interfaceName;

    /**
     * 类名称
     */
    private String className;

    /**
     * 请求的方法名
     */
    private String methodName;

    /**
     * 方法参数中的类
     */
    private Class<?>[] parameterTypes;

    /**
     * 请求的方法名中的参数具体。
     */
    private Object[] parameters;
}
