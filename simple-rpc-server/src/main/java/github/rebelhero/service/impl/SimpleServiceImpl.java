package github.rebelhero.service.impl;

import github.rebelhero.api.SimpleService;
import github.rebelhero.entity.Simple;
import lombok.extern.slf4j.Slf4j;

/**
 * 实现类
 *
 * @author rebelhero
 * @date 2020/11/25
 */
@Slf4j
public class SimpleServiceImpl implements SimpleService {

    /**
     * 打个招呼！
     *
     * @param simple 实体类
     * @return String
     * @date 2020/11/25
     */
    @Override
    public String hello(Simple simple) {
        log.info("hello呀！");
        String result = "打个招呼！";
        log.info("SimpleServiceImpl调皮地返回:{}", result);
        return result;
    }
}
