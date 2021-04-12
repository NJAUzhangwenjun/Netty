package cn.wjhub.netty.chatroom.server.handler;

import cn.wjhub.netty.chatroom.message.GroupQuitRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述：
 *
 * @ClassName GroupQuitRequestMessageHandler
 *
 * @Author 张文军
 * @Date 2021/4/11 16:12
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {

    }
}
