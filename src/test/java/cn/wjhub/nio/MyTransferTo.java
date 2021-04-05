package cn.wjhub.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 类描述：
 *
 * @ClassName MyTransferTo
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/6 1:18
 * @Version 1.0
 */

@Slf4j
public class MyTransferTo {

    public static void main(String[] args) {
        transferTo();
    }

    /**
     * 两个 channel 之间的数据传输
     *
     * @throws IOException
     */
    private static void transferTo(){
        try (FileChannel from = new FileInputStream("src\\main\\resources\\nio\\gatheringWrites.txt").getChannel();
             FileChannel to = new FileOutputStream("src\\main\\resources\\nio\\scatteringRead.txt.txt").getChannel()
        ) {
            from.transferTo(0, from.size(), to);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
