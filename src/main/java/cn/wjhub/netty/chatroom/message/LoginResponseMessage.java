package cn.wjhub.netty.chatroom.message;

import cn.wjhub.netty.chatroom.server.service.AbstractUserServiceFactory;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.ToString;

/**
 * @author user
 */
@Data
@ToString(callSuper = true)
public class LoginResponseMessage extends AbstractResponseMessage {

    public static void doLogin(ChannelHandlerContext ctx, LoginRequestMessage msg) {

        ctx.channel().eventLoop().submit(() -> {
            boolean login = AbstractUserServiceFactory.getUserService().login(msg.getUsername(), msg.getPassword());
            AbstractResponseMessage loginResponseMessage = null;
            if (login) {
                loginResponseMessage = new LoginResponseMessage(true, "login_success");
            } else {
                loginResponseMessage = new LoginResponseMessage(false, "login_failed");
            }
            ctx.channel().writeAndFlush(loginResponseMessage);
        });
    }

    public LoginResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return LoginResponseMessage;
    }
}
