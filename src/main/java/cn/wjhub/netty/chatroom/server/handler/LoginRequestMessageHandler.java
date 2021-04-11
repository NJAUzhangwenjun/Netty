package cn.wjhub.netty.chatroom.server.handler;

import cn.wjhub.netty.chatroom.message.AbstractResponseMessage;
import cn.wjhub.netty.chatroom.message.LoginRequestMessage;
import cn.wjhub.netty.chatroom.message.LoginResponseMessage;
import cn.wjhub.netty.chatroom.server.service.AbstractUserServiceFactory;
import cn.wjhub.netty.chatroom.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录处理器
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        /*处理连接请求*/
        boolean login = AbstractUserServiceFactory.getUserService().login(msg.getUsername(), msg.getPassword());
        AbstractResponseMessage loginResponseMessage = null;
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), msg.getUsername());
            loginResponseMessage = new LoginResponseMessage(true, "login_success");
        } else {
            loginResponseMessage = new LoginResponseMessage(false, "login_failed");
        }
        ctx.channel().writeAndFlush(loginResponseMessage);
    }
}
