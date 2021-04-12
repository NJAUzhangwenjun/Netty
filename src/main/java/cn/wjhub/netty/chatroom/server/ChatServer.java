package cn.wjhub.netty.chatroom.server;


import cn.wjhub.netty.chatroom.protocol.MessageCodecSharable;
import cn.wjhub.netty.chatroom.protocol.ProcotolFrameDecoder;
import cn.wjhub.netty.chatroom.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        /*自定义处理器*/
        LoginRequestMessageHandler LOGIN_REQUEST_MESSAGE_HANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHAT_REQUEST_MESSAGE_HANDLER = new ChatRequestMessageHandler();
        GroupCreateRequestMessageHandler GROUP_CREATE_REQUEST_MESSAGE_HANDLER = new GroupCreateRequestMessageHandler();
        GroupChatRequestMessageHandler GROUP_CHAT_REQUEST_MESSAGE_HANDLER = new GroupChatRequestMessageHandler();
        GroupMembersRequestMessageHandler GROUP_MEMBERS_REQUEST_MESSAGE_HANDLER = new GroupMembersRequestMessageHandler();
        GroupJoinRequestMessageMessageHandler GROUP_JOIN_REQUEST_MESSAGE_MESSAGE_HANDLER = new GroupJoinRequestMessageMessageHandler();
        GroupQuitRequestMessageHandler GROUP_QUIT_REQUEST_MESSAGE_HANDLER = new GroupQuitRequestMessageHandler();
        QuitRequestHandler QUIT_REQUEST_HANDLER = new QuitRequestHandler();


        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    /*日志处理器*/
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    /*帧解码器：解决粘包，半包问题*/
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    /*消息协议编码解码器*/
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    /*空闲状态处理*/
                    // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
                    // 5s 内如果没有收到 channel 的数据，会触发一个 IdleState#READER_IDLE 事件
                    ch.pipeline().addLast(new IdleStateHandler(5, 0, 0));
                    // ChannelDuplexHandler 可以同时作为入站和出站处理器
                    ch.pipeline().addLast(new ChannelDuplexHandler() {
                        // 用来触发特殊事件
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            // 触发了读空闲事件
                            if (event.state() == IdleState.READER_IDLE) {
                                log.debug("已经 5s 没有读到数据了");
                                ctx.channel().close();
                            }
                        }
                    });

                    ch.pipeline().addLast(LOGIN_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(CHAT_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(GROUP_CREATE_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(GROUP_CHAT_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(GROUP_MEMBERS_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(GROUP_JOIN_REQUEST_MESSAGE_MESSAGE_HANDLER);
                    ch.pipeline().addLast(GROUP_QUIT_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(QUIT_REQUEST_HANDLER);

                }
            });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
