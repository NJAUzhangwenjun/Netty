package cn.wjhub.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * 类描述：Selector 创建网络编程
 *
 * @ClassName Server
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/6 2:27
 * @Version 1.0
 */
@Slf4j
public class SelectorServer {
    public static void main(String[] args) {
        server();
    }


    private static void server() {
        /*创建服务器*/
        try (
                Selector selector = Selector.open();
                ServerSocketChannel ssc = ServerSocketChannel.open();
        ) {
            /*绑定服务器监听端口*/
            ssc.bind(new InetSocketAddress("localhost", 8080));
            /*设置非阻塞*/
            ssc.configureBlocking(false);

            /*将Channel注册到Selector上,返回结果是：SelectorKey：事件发生时返回，可以知道发生什么事件和那个Channel发生*/
            ssc.register(selector, SelectionKey.OP_ACCEPT, null);
            /*设置关注感兴趣的事件类型*/
            while (true) {
                selector.select();
                log.info("selector----after----");
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    log.info("key:{}", key);
                    //判断事件类型
                    /*如果是ServerSocketChannel 的连接事件*/
                    try {
                        if (key.isAcceptable()) {
                            //事件处理
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                            /*建立客户端连接*/
                            SocketChannel accept = serverSocketChannel.accept();
                            if (accept != null) {
                                accept.configureBlocking(false);
                                log.info("acceptChannel:{}", accept);
                                /*注册到Selector上*/
                                accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1 << 4));
                            }
                            /*SocketChannel的读取事件*/
                        } else if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            /** 对 Buffer 进行扩容*/
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() << 1);
                                buffer.flip();
                                newBuffer.put(buffer);
                                buffer.clear();
                                key.attach(newBuffer);
                            } else if (channel != null) {
                                //事件处理
                                handle(channel, buffer);
                            }
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        //事件处理
                        key.cancel();
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 读取数据事件处理
     *
     * @param accept
     * @param buffer
     */
    private static void handle(SocketChannel accept, ByteBuffer buffer) throws IOException {
        if (accept.read(buffer) == -1) {
            accept.close();
        } else {
            log.info("handle:{}", accept);
            buffer.flip();
            // debugRead(buffer);
            log.info("message: {}", Charset.defaultCharset().decode(buffer).toString());
            buffer.clear();
        }

    }
}
