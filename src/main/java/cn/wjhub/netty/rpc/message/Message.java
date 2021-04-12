package cn.wjhub.netty.rpc.message;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Message implements Serializable {

    /**
     * 根据消息类型字节，获得对应的消息 class
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResponseMessage = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupMembersRequestMessage = 12;
    public static final int GroupMembersResponseMessage = 13;
    public static final int PingMessage = 14;
    public static final int PongMessage = 15;
    /**
     * 请求类型 byte 值
     */
    public static final int RPC_MESSAGE_TYPE_REQUEST = 101;
    /**
     * 响应类型 byte 值
     */
    public static final int  RPC_MESSAGE_TYPE_RESPONSE = 102;

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(LoginRequestMessage, cn.wjhub.netty.chatroom.message.LoginRequestMessage.class);
        messageClasses.put(LoginResponseMessage, cn.wjhub.netty.chatroom.message.LoginResponseMessage.class);
        messageClasses.put(ChatRequestMessage, cn.wjhub.netty.chatroom.message.ChatRequestMessage.class);
        messageClasses.put(ChatResponseMessage, cn.wjhub.netty.chatroom.message.ChatResponseMessage.class);
        messageClasses.put(GroupCreateRequestMessage, cn.wjhub.netty.chatroom.message.GroupCreateRequestMessage.class);
        messageClasses.put(GroupCreateResponseMessage, cn.wjhub.netty.chatroom.message.GroupCreateResponseMessage.class);
        messageClasses.put(GroupJoinRequestMessage, cn.wjhub.netty.chatroom.message.GroupJoinRequestMessage.class);
        messageClasses.put(GroupJoinResponseMessage, cn.wjhub.netty.chatroom.message.GroupJoinResponseMessage.class);
        messageClasses.put(GroupQuitRequestMessage, cn.wjhub.netty.chatroom.message.GroupQuitRequestMessage.class);
        messageClasses.put(GroupQuitResponseMessage, cn.wjhub.netty.chatroom.message.GroupQuitResponseMessage.class);
        messageClasses.put(GroupChatRequestMessage, cn.wjhub.netty.chatroom.message.GroupChatRequestMessage.class);
        messageClasses.put(GroupChatResponseMessage, cn.wjhub.netty.chatroom.message.GroupChatResponseMessage.class);
        messageClasses.put(GroupMembersRequestMessage, cn.wjhub.netty.chatroom.message.GroupMembersRequestMessage.class);
        messageClasses.put(GroupMembersResponseMessage, cn.wjhub.netty.chatroom.message.GroupMembersResponseMessage.class);
        messageClasses.put(RPC_MESSAGE_TYPE_REQUEST, RpcRequestMessage.class);
        messageClasses.put(RPC_MESSAGE_TYPE_RESPONSE, RpcResponseMessage.class);
    }

}
