package cn.wjhub.netty.chatroom.message;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.System.out;

/**
 * @author user
 */
@Data
@ToString(callSuper = true)
@Slf4j
public class LoginRequestMessage extends Message {
    private String username;
    private String password;
    private volatile static CountDownLatch WAITING_LOGIN = new CountDownLatch(1);
    private static AtomicBoolean Logged = new AtomicBoolean(false);

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 客户端建立连接后处理登录
     *
     * @param ctx
     */
    public static void toLogin(ChannelHandlerContext ctx) {
        new Thread(() -> {
            // TODO:
            Scanner in = new Scanner(System.in);
            out.println("请输入用户名：");
            String username = in.nextLine().trim();
            out.println("请输入密码：");
            String password = in.nextLine().trim();
            /*将数据发送出去*/
            ctx.channel().writeAndFlush(new LoginRequestMessage(username, password));
            out.println("登录已发起，等待服务器响应 。。。");
            try {
                WAITING_LOGIN.await();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            if (!Logged.get()) {
                ctx.channel().close();
                return;
            }
            while (true) {
                System.out.println("\033[1;93;45m" + "==================================" + "\033[m");
                System.out.println("\033[1;93;45m" + "send [username] [content]" + "\033[m");
                System.out.println("\033[1;93;45m" + "gsend [group name] [content]" + "\033[m");
                System.out.println("\033[1;93;45m" + "gcreate [group name] [m1,m2,m3...]" + "\033[m");
                System.out.println("\033[1;93;45m" + "gmembers [group name]" + "\033[m");
                System.out.println("\033[1;93;45m" + "gjoin [group name]" + "\033[m");
                System.out.println("\033[1;93;45m" + "gquit [group name]" + "\033[m");
                System.out.println("\033[1;93;45m" + "quit" + "\033[m");
                System.out.println("\033[1;93;45m" + "==================================" + "\033[m");
                try {
                    String s = in.nextLine();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, "LoginRequestMessage").start();
    }

    /**
     * 处理服务器登录响应
     *
     * @param ctx
     * @param msg
     */
    public static void processingResponseMessage(ChannelHandlerContext ctx, Object msg) {
        new Thread(() -> {
            if ((msg instanceof LoginResponseMessage)) {
                LoginResponseMessage loginResponseMessage = (LoginResponseMessage) msg;
                if (loginResponseMessage.isSuccess()) {
                    /*控制台*/
                    Logged.set(true);
                    out.println("登录成功 : " + loginResponseMessage.getReason());
                }
                /*不管是否登录成功，都要唤醒输入线程*/
                WAITING_LOGIN.countDown();
            }

        }).start();
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
