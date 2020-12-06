package github.rebelhero.transport.impl;

import cn.hutool.core.util.ObjectUtil;
import github.rebelhero.client.NettyClient;
import github.rebelhero.discovery.ZkServiceDiscovery;
import github.rebelhero.entity.RpcRequest;
import github.rebelhero.entity.RpcResponse;
import github.rebelhero.loadbalance.LoadBalance;
import github.rebelhero.serializer.Serializer;
import github.rebelhero.transport.MessageTransport;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class MessageTransportImpl implements MessageTransport {

    private Serializer serializer;
    private static Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    public MessageTransportImpl(Serializer serializer) {
        this.serializer = serializer;
    }

    private Channel getChannel(InetSocketAddress inetSocketAddress) {
        String address = inetSocketAddress.toString();
        Channel channel = channelMap.get(address);
        if (ObjectUtil.isNotNull(channel)) {
            if (channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(address);
            }
        }
        NettyClient nettyClient = new NettyClient(serializer);
        channel = nettyClient.connect(inetSocketAddress);
        channelMap.put(address, channel);
        return channel;

    }

    /**
     * 发送请求。
     *
     * @param rpcRequest  数据请求
     * @param loadBalance 负载均衡。
     * @return CompletableFuture<RpcResponse>
     * @date 2020/11/30
     */
    @Override
    public CompletableFuture<RpcResponse> sendMessage(RpcRequest rpcRequest, LoadBalance loadBalance) {
        CompletableFuture<RpcResponse> completableFuture = new CompletableFuture<>();
        String rpcServiceName = rpcRequest.getInterfaceName();
        ZkServiceDiscovery zkServiceDiscovery = new ZkServiceDiscovery();
        // 得到提供服务的地址。
        InetSocketAddress inetSocketAddress = zkServiceDiscovery.lookupService(rpcServiceName, loadBalance);
        // 获得该地址的channel。
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            channel.writeAndFlush(rpcRequest)
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            log.info("客户端发送消息：{}", rpcRequest);
                        } else {
                            future.channel().close();
                            completableFuture.completeExceptionally(future.cause());
                            log.error("发送失败，错误原因为：", future.cause());
                        }
                    });
        }
        return completableFuture;
    }
}
