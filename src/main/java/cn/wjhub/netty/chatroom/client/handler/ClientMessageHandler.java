package cn.wjhub.netty.chatroom.client.handler;

import cn.wjhub.netty.chatroom.message.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.System.out;

/**
 * @author user
 */
@Data
@ToString(callSuper = true)
@Slf4j
public class ClientMessageHandler {
    private volatile static CountDownLatch WAITING_LOGIN = new CountDownLatch(1);
    private static AtomicBoolean Logged = new AtomicBoolean(false);

    /**
     * 发送消息线程（指令，消息）
     *
     * @param ctx
     */
    public static void receiveInstructionsAndSendMessages(ChannelHandlerContext ctx) {
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
                /*登录阻塞等待*/
                WAITING_LOGIN.await();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            if (!Logged.get()) {/*登录失败，关闭客户端连接*/
                out.println("登录失败！");
                ctx.channel().close();
                return;
            }
            while (true) {
                /*打印提示*/
                printCommandPromptMenu();
                Message message = null;
                try {
                    /*接受指令*/
                    String[] commands = in.nextLine().split(" ");
                    /*构建消息*/
                    message = getMessage(username,ctx, commands);
                    /*发送消息*/
                    if (message != null) {
                        ctx.writeAndFlush(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "LoginRequestMessage-receiveInstructionsAndSendMessages").start();
    }

    /**
     * 打印菜单
     */
    private static void printCommandPromptMenu() {
        out.println("请按照一下菜单输入操作指令：");
        out.println("\033[1;93;45m" + "==================================" + "\033[m");
        out.println("\033[1;93;45m" + "send [username] [content]" + "\033[m");
        out.println("\033[1;93;45m" + "gsend [group name] [content]" + "\033[m");
        out.println("\033[1;93;45m" + "gcreate [group name] [m1,m2,m3...]" + "\033[m");
        out.println("\033[1;93;45m" + "gmembers [group name]" + "\033[m");
        out.println("\033[1;93;45m" + "gjoin [group name]" + "\033[m");
        out.println("\033[1;93;45m" + "gquit [group name]" + "\033[m");
        out.println("\033[1;93;45m" + "quit" + "\033[m");
        out.println("\033[1;93;45m" + "==================================" + "\033[m");
    }

    /**
     * 根据指令构建消息
     *
     * @param username
     * @param ctx
     * @param commands
     * @return
     */
    private static Message getMessage(String username, ChannelHandlerContext ctx, String[] commands) {
        Message message = null;
        switch (commands[0]) {
            case "send": {
                message = new ChatRequestMessage(username, commands[1], commands[2]);
            }
            break;
            case "gsend": {
                message = new GroupChatRequestMessage(username, commands[1], commands[0]);
            }
            break;
            case "gcreate": {
                Set<String> set = new HashSet<>(Arrays.asList(commands[1].split(",")));
                set.add(username);
                message = new GroupCreateRequestMessage(username, set);
            }
            break;
            case "gmembers": {
                message = new GroupMembersRequestMessage(commands[1]);
            }
            break;
            case "gjoin": {
                message = new GroupJoinRequestMessage(username, commands[1]);
            }
            break;
            case "gquit": {
                message = new GroupQuitRequestMessage(username, commands[1]);
            }
            break;
            case "quit": {
                ctx.channel().close();
            }
            break;
            default:
                out.println("输入命令不正确，请重新输入！ " + commands[0]);
        }
        return message;
    }

    /**
     * 处理服务器登录响应
     *
     * @param ctx
     * @param msg
     */
    public static void processingResponseMessage(ChannelHandlerContext ctx, Object msg) {
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

    }

}
