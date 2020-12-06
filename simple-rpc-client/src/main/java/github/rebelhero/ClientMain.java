package github.rebelhero;

import github.rebelhero.api.SimpleService;
import github.rebelhero.entity.Simple;
import github.rebelhero.generator.RpcBeanNameGenerator;
import github.rebelhero.loadbalance.impl.RandomLoadBalance;
import github.rebelhero.proxy.ClientProxy;
import github.rebelhero.scan.KoScan;
import github.rebelhero.serializer.kryo.KryoSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
@KoScan(basePackages = {"github.rebelhero"})
public class ClientMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(ClientMain.class);
        KryoSerializer kryoSerializer = (KryoSerializer) applicationContext.getBean("kryo");
        ClientProxy clientProxy = new ClientProxy(kryoSerializer, new RandomLoadBalance());
        // 动态代理生成HelloService
        SimpleService simpleService = clientProxy.getProxy(SimpleService.class);
        String result = simpleService.hello(new Simple()
                .setName("rebelHero")
                .setContent("this is rebelHero"));
        log.info(result);
    }
}
