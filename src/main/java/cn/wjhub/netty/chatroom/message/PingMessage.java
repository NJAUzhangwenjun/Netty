package cn.wjhub.netty.chatroom.message;

/**
 * 类描述：
 *
 * @ClassName PingMessage
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/11 23:59
 * @Version 1.0
 */

public class PingMessage extends Message{

    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
