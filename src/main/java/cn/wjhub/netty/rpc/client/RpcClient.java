package cn.wjhub.netty.rpc.client;


import cn.wjhub.netty.rpc.handler.RpcResponseMessageHandler;
import cn.wjhub.netty.rpc.message.RpcRequestMessage;
import cn.wjhub.netty.rpc.protocol.MessageCodecSharable;
import cn.wjhub.netty.rpc.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author user
 */
@Slf4j
public class RpcClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

        // rpc 响应消息处理器，待实现
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        try {
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
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            /*test*/
            ChannelFuture channelFuture = channel.writeAndFlush(new RpcRequestMessage(
                    1,
                    "cn.wjhub.netty.rpc.service.HelloService",
                    "say", String.class,
                    new Class[]{String.class},
                    new Object[]{"Java"})).addListener(future -> {
                if (future.isSuccess()) {
                    log.info("future====success========={}", future.get());
                } else {
                    log.info("future=======error======{}", future.cause());
                }
            });
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
