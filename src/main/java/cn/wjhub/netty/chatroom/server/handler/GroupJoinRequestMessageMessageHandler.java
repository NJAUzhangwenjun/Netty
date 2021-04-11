package cn.wjhub.netty.chatroom.server.handler;

import cn.wjhub.netty.chatroom.message.GroupJoinRequestMessage;
import cn.wjhub.netty.chatroom.message.GroupJoinResponseMessage;
import cn.wjhub.netty.chatroom.server.session.Group;
import cn.wjhub.netty.chatroom.server.session.GroupSessionFactory;
import cn.wjhub.netty.chatroom.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 类描述：
 *
 * @ClassName GroupJoinRequestMessageMessageHandler
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/11 16:15
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupJoinRequestMessageMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Group group = GroupSessionFactory.getGroupSession().joinMember(groupName, msg.getUsername());
        Channel channel = ctx.channel();
        if (group != null) {
            Set<String> members = GroupSessionFactory.getGroupSession().getMembers(groupName);
            String username = msg.getUsername();

            for (String member : members) {
                Channel c = SessionFactory.getSession().getChannel(member);
                if (!channel.equals(c)) {
                    c.writeAndFlush(new GroupJoinResponseMessage(true, username + " 加入了群聊！"));
                }
            }
        } else {
            ctx.channel().writeAndFlush(new GroupJoinResponseMessage(true, " 加入群聊失败！"));
        }
    }
}
