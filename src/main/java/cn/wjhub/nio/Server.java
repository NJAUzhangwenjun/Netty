package cn.wjhub.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import static cn.wjhub.utils.ByteBufferUtil.debugRead;
import static java.nio.ByteBuffer.allocate;

/**
 * 类描述：NIO 创建网络编程
 *
 * 使用单线程 NIO创建服务器连接
 *
 * @ClassName Server
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/6 2:27
 * @Version 1.0
 */
@Slf4j
public class Server {
    public static void main(String[] args) {
        server();
    }

    /**
     * 使用单线程 NIO创建服务器连接
     */
    private static void server() {
        final ArrayList<SocketChannel> socketChannels = new ArrayList<>();
        /*创建服务器*/
        try (ServerSocketChannel open = ServerSocketChannel.open()) {
            /*绑定服务器监听端口*/
            open.bind(new InetSocketAddress("localhost", 8080));
            /*设置非阻塞*/
            open.configureBlocking(false);
            /*建立客户端连接*/
            while (true) {
                SocketChannel accept = open.accept();
                if (accept != null) {
                    accept.configureBlocking(false);
                    socketChannels.add(accept);
                }
                /*处理连接*/
                for (SocketChannel channel : socketChannels) {
                    handle(channel);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 处理客户端连接
     *
     * @param accept
     */
    private static void handle(SocketChannel accept) {
            ByteBuffer buffer = allocate(16);
            try {
                if (accept.read(buffer) > 0) {
                    log.info("handle:{}", accept);
                    buffer.flip();
                    debugRead(buffer);
                    buffer.clear();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
    }
}
