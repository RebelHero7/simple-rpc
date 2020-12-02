package github.rebelhero;

import github.rebelhero.api.SimpleService;
import github.rebelhero.entity.Simple;
import github.rebelhero.proxy.ClientProxy;
import github.rebelhero.serializer.kryo.KryoSerializer;
import github.rebelhero.serializer.protostuff.ProtostuffSerializer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class ClientMain {

    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy(new KryoSerializer());
        // 动态代理生成HelloService
        SimpleService simpleService = clientProxy.getProxy(SimpleService.class);
        String result = simpleService.hello(new Simple()
                .setName("rebelHero")
                .setContent("this is rebelHero"));
        log.info(result);
    }
}
