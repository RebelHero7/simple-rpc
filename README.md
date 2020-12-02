# simple-rpc
一个简单的基于 Netty + ZooKeeper 的Rpc

> 使用说明
1. **开启Zookeeper**
    
    推荐使用docker下载Zookeeper并使用, 方法如下：
    >  docker search zookeeper
    > 
    >  docker pull zookeeper
    >                                               
    >  docker run --name my-zookeeper --restart always -d zookeeper
    
    上述命令会自动绑定端口，当然也推荐自己指定端口，如 -p 2181:2181
     
    进入其内部可以使用:
    > docker exec -it my-zookeeper bash
2. **开启服务端**
    
    可直接运行simple-rpc-server 下的ServerMain
    
    查看服务端是否注册到ZooKeeper可使用：
    > docker run -it --rm --link my-zookeeper:zookeeper zookeeper zkCli.sh -server zookeeper
    
    开启一个ZooKeeper的客户端并进入其中查看是否有/simple-rpc节点及其子节点
    
3. **开启客户端**
   
   可直接运行simple-rpc-client的clientMain
   
   如果有客户端收发打印日志即表示成功。