package cn.wjhub.netty.chatroom.message;

import cn.wjhub.netty.rpc.message.Message;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author user
 */
@Data
@ToString(callSuper = true)
@Slf4j
public class LoginRequestMessage extends Message {
    private String username;
    private String password;
    private volatile static CountDownLatch WAITING_LOGIN = new CountDownLatch(1);
    private static AtomicBoolean Logged = new AtomicBoolean(false);

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
