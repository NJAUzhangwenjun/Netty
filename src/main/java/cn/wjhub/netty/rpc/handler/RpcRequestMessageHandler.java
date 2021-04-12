package cn.wjhub.netty.rpc.handler;

import cn.wjhub.netty.rpc.message.RpcRequestMessage;
import cn.wjhub.netty.rpc.message.RpcResponseMessage;
import cn.wjhub.netty.rpc.service.ServicesFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类描述：
 *
 * @ClassName RpcRequestMessageHandler
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/12 16:23
 * @Version 1.0
 */
@Slf4j
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) {
        RpcResponseMessage responseMessage = new RpcResponseMessage();
        try {
            Object service = ServicesFactory.getService(Class.forName(msg.getInterfaceName()));
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object invoke = method.invoke(service, msg.getParameterValue());
            responseMessage.setReturnValue(invoke);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            responseMessage.setReturnValue(null);
            responseMessage.setExceptionValue(e);
            log.error("msg={};Exception={}", msg, e.getMessage());
        } finally {
            ctx.writeAndFlush(responseMessage);
        }
    }

  /*  public static void main(String[] args) throws Exception{
        RpcRequestMessage msg = new RpcRequestMessage(
                1, "cn.wjhub.netty.rpc.service.HelloService",
                "say", String.class, new Class[]{String.class}, new Object[]{"Java"});
        Object service = ServicesFactory.getService(Class.forName(msg.getInterfaceName()));
        Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
        Object invoke = method.invoke(service,msg.getParameterValue());
        System.out.println("invoke = " + invoke);

    }*/
}
