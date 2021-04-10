package cn.wjhub.netty.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * 类描述：netty 客户端
 *
 * @ClassName HelloClient
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/7 13:36
 * @Version 1.0
 */
@Slf4j
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup executors = new NioEventLoopGroup(2);
        Channel channel = new Bootstrap()
                .group(executors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 8080)
                .sync()
                .channel();
        log.info("channel--->{}",channel);
        /*向服务器发送数据*/
        executors.next().submit(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String s = scanner.nextLine();
                if (s.trim().equals("q")) {
                    channel.close();
                    break;
                }
                channel.writeAndFlush(s);
            }
        }, "MyEventLoopServer-Input");
        ChannelFuture channelFuture = channel.closeFuture();
        log.info("waiting----close");
        channelFuture.sync();
        executors.shutdownGracefully();
        log.info("---CLOSE---");
    }
}
