package cn.wjhub.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import static cn.wjhub.utils.ByteBufferUtil.debugAll;

/**
 * 类描述：
 * 对 NIO 的测试
 *
 * @ClassName TestByteBuffer
 *
 * @Author 张文军
 * @Date 2021/4/5 7:17
 * @Version 1.0
 */
@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
//        printFiles();
//        byteBufferType();
//        bufferRead();
//        BufferCast();
//        GatheringWrites();
        stickyBagHalfBag();
    }

    /**
     * @description: printFiles 打印文件
     * @return: void
     * @author user
     * @date: 2021/4/5 22:19
     */
    private static void printFiles() {
        try (FileChannel channel = new FileInputStream("src\\main\\resources\\nio\\data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            int read;
            while ((read = channel.read(buffer)) != -1) {
                log.info("读取结果：{}", new String(buffer.array(), 0, read));
                buffer.clear();
            }
        } catch (IOException e) {
            log.error("出现了异常：{}", e.getMessage());
        }
    }

    /**
     * @description: byteBufferType ByteBuffer 类型
     * @return: void
     * @author user
     * @date: 2021/4/5 22:24
     */
    private static void byteBufferType() {
        //HeapByteBuffer
        ByteBuffer allocate = ByteBuffer.allocate(16);
        //DirectByteBuffer
        ByteBuffer direct = ByteBuffer.allocateDirect(16);

        log.info(allocate.getClass().getTypeName());
        log.info(direct.getClass().getTypeName());
    }

    /**
     * @description: bufferRead
     * @param:
     * @return: void
     * @author user
     * @date: 2021/4/5 22:42
     */
    private static void bufferRead() {
        ByteBuffer allocate = ByteBuffer.allocate(16);
        allocate.put(new byte[]{'a', 'b', 'c', 'd'});
        allocate.flip();
        debugAll(allocate);
        allocate.get(new byte[2]);
        debugAll(allocate);
        allocate.rewind();
        debugAll(allocate);


    }

    private static void BufferCast() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put("wjhub.cn".getBytes());
        debugAll(buffer);

        ByteBuffer encode = StandardCharsets.UTF_8.encode("wjhub.cn");
        debugAll(encode);
    }

    /**
     * @description: scatteringRead 分散读取
     * @param:
     * @return: void
     * @author user
     * @date: 2021/4/5 23:45
     */
    private static void scatteringRead() {
        try {
            FileChannel channel = new FileInputStream("src\\main\\resources\\nio\\scatteringRead.txt").getChannel();
            ByteBuffer bf1 = ByteBuffer.allocate(3);
            ByteBuffer bf2 = ByteBuffer.allocate(3);
            ByteBuffer bf3 = ByteBuffer.allocate(5);
            ByteBuffer[] dsts = {bf1, bf2, bf3};
            channel.read(dsts);
            for (int i = 0; i < dsts.length; i++) {
                dsts[i].flip();
                debugAll(dsts[i]);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * @description: GatheringWrites 集中写
     * @param:
     * @return: void
     * @author user
     * @date: 2021/4/5 23:51
     */
    private static void GatheringWrites() {
        ByteBuffer bf1 = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer bf2 = StandardCharsets.UTF_8.encode("world");
        ByteBuffer bf3 = StandardCharsets.UTF_8.encode("文军");

        try (FileChannel channel = new RandomAccessFile("src\\main\\resources\\nio\\gatheringWrites.txt", "rw").getChannel()) {
            ByteBuffer[] srcs = {bf1, bf2, bf3};
            channel.write(srcs);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
    网络上有多条数据发送给服务端，数据之间使用 \n 进行分隔
    但由于某种原因这些数据在接收时，被进行了重新组合，例如原始数据有3条为

    * Hello,world\n
    * I'm zhangsan\n
    * How are you?\n

    变成了下面的两个 byteBuffer (黏包，半包)

    * Hello,world\nI'm zhangsan\nHo
    * w are you?\n

    现在要求你编写程序，将错乱的数据恢复成原始的按 \n 分隔的数据
    */

    public static void stickyBagHalfBag() {
        /** 模拟 网络数据*/
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\nhaha!\n".getBytes());
        split(source);
    }

    /**
     * @description: split 将网络数据处理
     * 1. 还原原始数据格式
     * @param: source
     * @return: void
     * @author user
     * @date: 2021/4/5 23:56
     */
    private static void split(ByteBuffer source) {
        source.flip();
        int oldLimit = source.limit();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        for (int i = 0; i < oldLimit; i++) {
            buffer.put(source.get(i));
            if ('\n' == source.get(i)) {
                buffer.flip();
                debugAll(buffer);
                buffer.clear();
            }
        }
        source.position(oldLimit - buffer.position());
        source.compact();
    }

    private static void fileChannel() {
        try (FileChannel channel = new RandomAccessFile("", "rw").getChannel()) {

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
