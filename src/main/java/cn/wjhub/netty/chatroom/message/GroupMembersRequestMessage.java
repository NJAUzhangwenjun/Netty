package cn.wjhub.netty.chatroom.message;

import cn.wjhub.netty.rpc.message.Message;
import lombok.Data;
import lombok.ToString;

/**
 * @author user
 */
@Data
@ToString(callSuper = true)
public class GroupMembersRequestMessage extends Message {
    private String groupName;

    public GroupMembersRequestMessage(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int getMessageType() {
        return GroupMembersRequestMessage;
    }
}
