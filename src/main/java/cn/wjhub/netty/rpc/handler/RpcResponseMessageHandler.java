package cn.wjhub.netty.rpc.handler;

import cn.wjhub.netty.rpc.message.RpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 类描述：
 *
 * @ClassName RpcResponseMessageHandler
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/12 16:26
 * @Version 1.0
 */

public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {

    }
}
