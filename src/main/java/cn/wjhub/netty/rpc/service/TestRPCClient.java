package cn.wjhub.netty.rpc.service;

import static cn.wjhub.netty.rpc.client.RpcClientManager.GetProxyService;

/**
 * 类描述：自定义 RPC框架使用测试
 *
 * @ClassName TestRPCClient
 *
 * @Author 张文军
 * @Date 2021/4/13 4:10
 * @Version 1.0
 */

public class TestRPCClient {
    public static void main(String[] args) {
        HelloService service = GetProxyService(HelloService.class);
        System.out.println(service.say("zhangsan"));
        System.out.println(service.say("wangwu"));
    }
}
