package cn.wjhub.netty.chatroom.message;

import cn.wjhub.netty.rpc.message.Message;

/**
 * 类描述：
 *
 * @ClassName PongMessage
 *
 * @Author 张文军
 * @Date 2021/4/12 0:02
 * @Version 1.0
 */

public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
