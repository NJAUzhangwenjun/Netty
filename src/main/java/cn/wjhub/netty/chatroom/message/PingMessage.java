package cn.wjhub.netty.chatroom.message;

import cn.wjhub.netty.rpc.message.Message;

/**
 * 类描述：
 *
 * @ClassName PingMessage
 *
 * @Author 张文军
 * @Date 2021/4/11 23:59
 * @Version 1.0
 */

public class PingMessage extends Message {

    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
