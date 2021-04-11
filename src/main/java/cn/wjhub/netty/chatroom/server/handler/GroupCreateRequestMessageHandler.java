package cn.wjhub.netty.chatroom.server.handler;

import cn.wjhub.netty.chatroom.message.GroupChatResponseMessage;
import cn.wjhub.netty.chatroom.message.GroupCreateRequestMessage;
import cn.wjhub.netty.chatroom.message.GroupCreateResponseMessage;
import cn.wjhub.netty.chatroom.server.session.Group;
import cn.wjhub.netty.chatroom.server.session.GroupSessionFactory;
import cn.wjhub.netty.chatroom.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 类描述：
 *
 * @ClassName GroupCreateRequestMessageHandler
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/11 16:15
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        Group group = GroupSessionFactory.getGroupSession().createGroup(groupName, members);
        if (group == null) {
            ctx.channel().writeAndFlush(new GroupChatResponseMessage(true, groupName + " 创建成功！"));
            for (String member : members) {
                SessionFactory.getSession().getChannel(member).writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入 " + msg + " 群聊！"));
            }
        } else {
            ctx.channel().writeAndFlush(new GroupChatResponseMessage(false, groupName + " 创建群聊失败！"));
        }
    }
}
