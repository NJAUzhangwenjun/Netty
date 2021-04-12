package cn.wjhub.netty.rpc.service;

/**
 * 类描述：Server 服务器对接口的实现
 *
 * @ClassName HelloServiceImpl
 *
 * @Author 张文军
 * @Date 2021/4/12 16:30
 * @Version 1.0
 */

public class HelloServiceImpl implements HelloService {
    @Override
    public String say(String word) {
        return "Server HelloServiceImpl.say:--->" + word +" hi!";
    }
}
