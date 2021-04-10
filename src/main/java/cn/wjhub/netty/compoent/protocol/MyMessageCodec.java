package cn.wjhub.netty.compoent.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 类描述：自定义聊天信息 编解码器
 *
 * @ClassName MyMessageCodec
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/10 12:39
 * @Version 1.0
 */
@Slf4j
public class MyMessageCodec extends ByteToMessageCodec<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        log.info("--------encode----------");
        // 1. 4 字节的魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1 字节的版本,
        out.writeByte(1);
        // 3. 1 字节的序列化方式 jdk 0 , json 1
        out.writeByte(0);
        // 4. 1 字节的指令类型
        out.writeByte(0);
        // 5. 4 个字节
//        out.writeInt(msg.length() & (~(~0 << 4)));
        // 无意义，对齐填充
        /*6. *序列化数据*/
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        new ObjectOutputStream(stream).writeObject(msg);

        /*7.数据长度*/
        out.writeInt(stream.toByteArray().length);
        /*8. */
        out.writeBytes(stream.toByteArray());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.info("--------decode----------");
        int magicNumber = in.readInt();
        byte version = in.readByte();
        byte instructionType = in.readByte();
        int b = in.readByte();
        int length = in.readInt();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        in.readBytes(buf, 0, length);

        String s = buf.toString();
        log.info("magicNumber-->={},version--->={},instructionType--->={},b--->={},length--->={},context--->={}", magicNumber, version, instructionType, b, length, s);
        out.add(s);
    }


    public static void main(String[] args) throws Exception {
        MyMessageCodec MESSAGE_CODEC = new MyMessageCodec();
        LoggingHandler LOGGINGHANDLER = new LoggingHandler(LogLevel.DEBUG);
        EmbeddedChannel channel = new EmbeddedChannel(LOGGINGHANDLER,new LengthFieldBasedFrameDecoder(1024,7,4,0,0), MESSAGE_CODEC);
//        channel.writeOutbound("hello----Java");
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        MESSAGE_CODEC.encode(null, "hello----Java", buf);
        channel.writeInbound(buf);
    }

}
