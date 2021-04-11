package cn.wjhub.netty.chatroom.server.handler;

import cn.wjhub.netty.chatroom.message.ChatRequestMessage;
import cn.wjhub.netty.chatroom.message.ChatResponseMessage;
import cn.wjhub.netty.chatroom.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * ChatRequestMessageHandler
 * 处理： 创建会话连接会话请求
 * @author user
 */
@Slf4j
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (channel != null) {
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(),msg.getContent()));
        } else {
            ctx.channel().writeAndFlush(to + " 用户不在线！");
        }
    }
}
