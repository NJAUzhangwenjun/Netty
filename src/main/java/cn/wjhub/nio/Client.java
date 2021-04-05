package cn.wjhub.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 类描述：
 *
 * @ClassName Client
 * @Description 客户端
 * @Author 张文军
 * @Date 2021/4/6 3:40
 * @Version 1.0
 */
@Slf4j
public class Client {
    public static void main(String[] args) throws InterruptedException {
        /*模拟启动五个客户端*/
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                sendMessage(Thread.currentThread().getName());
            }).start();
        }
        Thread.sleep(30000);
    }

    private static void sendMessage(String message) {
        try (SocketChannel client = SocketChannel.open()) {
            client.connect(new InetSocketAddress("localhost", 8080));
            /*每个客户端发送五次数据*/
            for (int i = 0; i < 5; i++) {
                ByteBuffer buffer = Charset.defaultCharset().encode(message + ":" + i);
                client.write(buffer);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
