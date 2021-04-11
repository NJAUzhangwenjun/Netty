package cn.wjhub.netty.chatroom.server.handler;

import cn.wjhub.netty.chatroom.message.GroupChatRequestMessage;
import cn.wjhub.netty.chatroom.message.GroupChatResponseMessage;
import cn.wjhub.netty.chatroom.server.session.GroupSessionFactory;
import cn.wjhub.netty.chatroom.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 类描述：
 *
 * @ClassName GroupChatRequestMessageHandler
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/11 16:15
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        List<Channel> membersChannel = GroupSessionFactory.getGroupSession().getMembersChannel(groupName);
        String from = msg.getFrom();
        Channel userChannel = SessionFactory.getSession().getChannel(from);
        if (!membersChannel.isEmpty()) {
            for (Channel channel : membersChannel) {
                if (!userChannel.equals(channel)) {
                    channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent()));
                }
            }
        } else {
            ctx.channel().writeAndFlush(msg.getGroupName() + " 该群不存在或无用户在线！");
        }

    }
}
