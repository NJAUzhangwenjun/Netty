package cn.wjhub.netty.rpc.handler;

import cn.wjhub.netty.rpc.message.RpcRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 类描述：
 *
 * @ClassName RpcRequestMessageHandler
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/12 16:23
 * @Version 1.0
 */

public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) throws Exception {

    }
}
