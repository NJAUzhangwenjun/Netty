package cn.wjhub.netty.rpc.client;


import cn.wjhub.netty.rpc.handler.RpcResponseMessageHandler;
import cn.wjhub.netty.rpc.message.RpcRequestMessage;
import cn.wjhub.netty.rpc.protocol.MessageCodecSharable;
import cn.wjhub.netty.rpc.protocol.ProcotolFrameDecoder;
import cn.wjhub.netty.rpc.protocol.SequenceIdGenerator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author user
 */
@Slf4j
public class RpcClientManager {
    private volatile static Channel channel = null;
    /**
     * 用来存储promise的集合（将结果发出去后可以接受）
     */
    public static final Map<Integer, Promise<Object>> PROMISE_MAP = new ConcurrentHashMap<>();


    /**
     * 获取单例 channel
     *
     * @return
     */
    private static Channel getChannel() {
        if (channel == null) {
            synchronized (RpcClientManager.class) {
                if (channel == null) {
                    initChannel();
                }
            }
        }
        return channel;
    }

    private RpcClientManager() {
    }

    /**
     * 客户端网络链接初始化
     */
    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        // rpc 响应消息处理器，待实现
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProcotolFrameDecoder());
                ch.pipeline().addLast(LOGGING_HANDLER);
                ch.pipeline().addLast(MESSAGE_CODEC);
                ch.pipeline().addLast(RPC_HANDLER);
            }
        });
        try {
            channel = bootstrap.connect("localhost", 8080).sync().channel();
            /*异步关闭*/
            channel.closeFuture().addListener(promise -> {
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            log.error("client error", e);
        }
    }

    /**
     * 代理类 通过JDK动态代理
     * 调用远程方法之前进行封装代理
     *
     * @param serviceClass 代理对象类型 Class
     * @param <T>          代理对象类型
     * @return 代理后的代理对象
     */
    public static <T> T GetProxyService(Class<T> serviceClass) {
        /*JDK代理*/
        Object proxyInstance = Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass},
                /*方法调用时增强的部分*/
                (proxy, method, args) -> {
                    /*将调用方法换为代理对象*/
                    RpcRequestMessage msg = new RpcRequestMessage(
                            SequenceIdGenerator.nextId(),
                            serviceClass.getName(),
                            method.getName(),
                            method.getReturnType(),
                            method.getParameterTypes(),
                            args);
                    /**将消息发送出去*/
                    getChannel().writeAndFlush(msg);
                    /**
                     * 接收结果
                     * */
                    DefaultPromise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
                    PROMISE_MAP.put(msg.getSequenceId(), promise);
                    /*在远程调用到结果前阻塞当前线程*/
                    promise.await();
                    if (promise.isSuccess()) {
                        // 调用正常
                        return promise.getNow();
                    }
                    // 调用失败
                    throw new RuntimeException(promise.cause());

                });
        return (T) proxyInstance;
    }
}
