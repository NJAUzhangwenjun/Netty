package cn.wjhub.netty.chatroom.message;

import cn.wjhub.netty.rpc.message.Message;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupQuitRequestMessage extends Message {
    private String groupName;

    private String username;

    public GroupQuitRequestMessage(String username, String groupName) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }
}
