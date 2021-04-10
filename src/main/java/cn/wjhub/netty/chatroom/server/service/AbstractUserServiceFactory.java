package cn.wjhub.netty.chatroom.server.service;

/**
 * @author user
 */
public abstract class AbstractUserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
