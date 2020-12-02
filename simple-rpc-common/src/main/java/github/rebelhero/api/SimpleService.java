package github.rebelhero.api;

import github.rebelhero.entity.Simple;

/**
 * @author rebelhero
 * @date 2020/11/25
 */
public interface SimpleService {

    /**
     * 打个招呼！
     *
     * @param simple 实体类
     * @return String
     * @date 2020/11/25
     */
    String hello(Simple simple);
}
