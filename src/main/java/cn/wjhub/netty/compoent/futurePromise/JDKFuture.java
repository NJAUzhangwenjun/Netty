package cn.wjhub.netty.compoent.futurePromise;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * 类描述：
 *
 * @ClassName JDKFuture
 * @Description TODO
 * @Author 张文军
 * @Date 2021/4/9 0:14
 * @Version 1.0
 */
@Slf4j
public class JDKFuture {
    public static void main(String[] args) {
        ExecutorService fixedThreadPool = newFixedThreadPool(2);
        Future<Integer> future = fixedThreadPool.submit(() -> {
            sleep(20);
            int i = 1 + 1;
            log.info("integer = {}",i);
            return i;
        });
        try {
            log.info("before---->get");
            Integer integer = future.get();
            log.info("integer = {}", integer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
