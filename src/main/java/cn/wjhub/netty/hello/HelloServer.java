package cn.wjhub.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述：netty 入门程序
 *
 * @ClassName HelloServer
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/7 3:53
 * @Version 1.0
 */
@Slf4j
public class HelloServer {
    public static void main(String[] args) {
        /*启动器，负责管理，组装netty组件，启动服务器*/
        new ServerBootstrap()
                /*group ：负责创建selector和线程*/
                .group(new NioEventLoopGroup())
                /*负责服务器的ServerSocket实现 nio，bio，aio*/
                .channel(NioServerSocketChannel.class)
                /*负责具体 channel 事件的处理*/
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                /*添加具体的handler*/
                                /*将byteBuf转换为String*/
                                ch.pipeline().addLast(new StringDecoder());
                                /*自定义处理器*/
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        log.info("message:->{}",msg);
                                    }
                                });
                            }
                        }).bind(8080);

    }
}
