package cn.wjhub.netty.rpc.service;

/**
 * 类描述：
 *
 * @ClassName HelloServiceImpl
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/12 16:30
 * @Version 1.0
 */

public class HelloServiceImpl implements HelloService {
    @Override
    public String say(String word) {
        return "HelloServiceImpl.say:--->" + word;
    }
}
