package cn.wjhub.netty.chatroom.server.handler;

import cn.wjhub.netty.chatroom.message.GroupMembersRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述：
 *
 * @ClassName GroupMembersRequestMessageHandler
 *
 * @Author 张文军
 * @Date 2021/4/11 16:15
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupMembersRequestMessageHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) throws Exception {

    }
}
