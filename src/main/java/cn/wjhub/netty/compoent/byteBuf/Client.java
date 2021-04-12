package cn.wjhub.netty.compoent.byteBuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Random;

/**
 * 类描述：
 *
 * @ClassName Client
 *
 * @Author 张文军
 * @Date 2021/4/10 0:23
 * @Version 1.0
 */

public class Client {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);
        ChannelFuture future = new Bootstrap()
                .group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                for (int i = 0; i < 5; i++) {
                                    ctx.channel().writeAndFlush(send(i));
                                }
                            }
                        });
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    }
                }).connect("localhost", 8080);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                loopGroup.shutdownGracefully();
            }
        });
    }

    private static ByteBuf send(int j) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        Random random = new Random();
        int n = random.nextInt(17);
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < n; i++) {
            message.append('a');
        }
        return buf.writeInt(message.length()).writeBytes(message.toString().getBytes());
    }
}
