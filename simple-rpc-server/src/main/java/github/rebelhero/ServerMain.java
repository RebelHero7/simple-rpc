package github.rebelhero;

import github.rebelhero.api.SimpleService;
import github.rebelhero.serializer.kryo.KryoSerializer;
import github.rebelhero.server.SimpleRpcServer;
import github.rebelhero.service.impl.SimpleServiceImpl;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
public class ServerMain {

    public static void main(String[] args) {
        SimpleService simpleService = new SimpleServiceImpl();

        KryoSerializer serializer = new KryoSerializer();
        SimpleRpcServer simpleRpcServer = new SimpleRpcServer(7777, serializer);
        simpleRpcServer.registry(simpleService, SimpleService.class);
    }
}
