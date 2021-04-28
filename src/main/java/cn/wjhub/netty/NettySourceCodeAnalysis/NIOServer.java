package cn.wjhub.netty.NettySourceCodeAnalysis;

import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 类描述：
 * NIO 的启动流程
 *
 * @ClassName NIOServer
 * @Author 张文军
 * @Date 2021/4/14 21:02
 * @Version 1.0
 */

public class NIOServer {
    public static void main(String[] args) throws Exception {
        /**
         * 创建一个selector
         * 创建一个channel
         * channel 注册到 selector 和 NioServerSocketChannel
         * 绑定端口
         * 注册关注事件
         */
        Selector selector = Selector.open();
        NioServerSocketChannel att = new NioServerSocketChannel();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, att);
        serverSocketChannel.bind(new InetSocketAddress(80800));
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
    }


}
