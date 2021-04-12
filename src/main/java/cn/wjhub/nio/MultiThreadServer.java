package cn.wjhub.nio;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.wjhub.utils.ByteBufferUtil.debugAll;

/**
 * 类描述：多线程 多路复用 IO server
 *
 * @ClassName MultiThreadServer
 *
 * @Author 张文军
 * @Date 2021/4/6 22:58
 * @Version 1.0
 */

@Slf4j
public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        Selector boss = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress("localhost", 8080));
        ssc.register(boss, SelectionKey.OP_ACCEPT);
        /*创建固定数量的worker，用来管理channel读写*/
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }
        while (true) {
            log.info("server select....");
            boss.select();
            Iterator<SelectionKey> keys = boss.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                /*如果是Accept事件*/
                if (key.isAcceptable()) {
                    log.info("isAcceptable ....");
                    /*建立连接事件*/
                    //serverSocketChannel:就是上面放入的ssc，也可以直接使用ssc（如果只有一个serverSocketChannel的话）
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);
                    /*将当前socketChannel注册到worker上*/
                    workers[count.getAndIncrement() % workers.length].register(sc);
                }
            }

        }
    }

    @Slf4j
    @Data
    static class Worker implements Runnable {
        private Thread thread;
        private Selector worker;
        private String name;
        private volatile boolean start;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel sc) throws IOException {
            /*确保只执行一次*/
            if (!this.start) {
                log.info("worker is register ...");
                this.thread = new Thread(this, this.name);
                this.worker = Selector.open();
                thread.start();
                this.start = true;
            }
            queue.add(() -> {
                /*将当前socketChannel注册到当前worker上*/
                try {
                    sc.register(worker, SelectionKey.OP_READ, ByteBuffer.allocate(16));
                } catch (ClosedChannelException e) {
                    log.error(e.getMessage());
                }
            });
            /*唤醒workerSelector的阻塞*/
            worker.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    log.info("worker is select ...");
                    worker.select();
                    /*执行注册socketChannel*/
                    if (queue.peek() != null) {
                        queue.poll().run();
                    }
                    Iterator<SelectionKey> keys = worker.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        keys.remove();
                        /*如果是读事件*/
                        if (key.isReadable()) {
                            log.info("worker isReadable ...");
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            /*读取处理*/
                            if (channel.read(buffer) == -1) {
                                channel.close();
                                continue;
                            }
                            buffer.flip();
                            debugAll(buffer);
                        } else if (key.isWritable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            /*ByteBuffer buffer = (ByteBuffer) key.attachment();*/
                            ByteBuffer buffer = Charset.defaultCharset().encode("hello world wjhub.cn");
                            /*写入处理*/
                            channel.write(buffer);
                        }
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
