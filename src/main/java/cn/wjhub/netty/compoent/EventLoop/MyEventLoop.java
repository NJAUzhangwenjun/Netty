package cn.wjhub.netty.compoent.EventLoop;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：EventLoop 组件
 *
 * @ClassName MyEventLoop
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/7 15:38
 * @Version 1.0
 */
@Slf4j
public class MyEventLoop {
    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);
        eventLoopGroup.next().submit(() -> log.error("thread:->{}", Thread.currentThread().getName()));
        eventLoopGroup.scheduleAtFixedRate(() -> log.error("thread:->{}", Thread.currentThread().getName()), 1, 1, TimeUnit.SECONDS);
    }
}
