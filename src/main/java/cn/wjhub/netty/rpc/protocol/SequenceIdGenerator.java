package cn.wjhub.netty.rpc.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author user
 */
public abstract class SequenceIdGenerator {
    private static final AtomicInteger ID = new AtomicInteger();

    public static int nextId() {
        return ID.incrementAndGet();
    }
}
