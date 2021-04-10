package cn.wjhub.netty.compoent.EventLoop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * 类描述：
 *
 * @ClassName MyEventLoopServer
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/8 17:00
 * @Version 1.0
 */
@Slf4j
public class MyEventLoopServer {
    public static void main(String[] args) {
        ChannelFuture channelFuture = new ServerBootstrap()
                /*前一个负责accept连接，后一个负责处理连接后的channel数据处理*/
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.error("message:-->{}", (((ByteBuf) msg).toString(Charset.defaultCharset())));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
