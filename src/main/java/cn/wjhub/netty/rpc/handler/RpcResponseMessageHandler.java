package cn.wjhub.netty.rpc.handler;

import cn.wjhub.netty.rpc.message.RpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import static cn.wjhub.netty.rpc.client.RpcClientManager.PROMISE_MAP;

/**
 * 类描述：
 *
 * @ClassName RpcResponseMessageHandler
 *
 * @Author 张文军
 * @Date 2021/4/12 16:26
 * @Version 1.0
 */
@Slf4j
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        log.info("返回响应:={}", msg);
        Promise<Object> promise = PROMISE_MAP.remove(msg.getSequenceId());
        if (promise != null) {
            /*不管调用一下那个方法，都会让 promise.wait()结束等待*/
            if (msg.getExceptionValue() != null) {
                promise.setFailure(msg.getExceptionValue());
            } else {
                promise.setSuccess(msg.getReturnValue());
            }
        }

    }


}
