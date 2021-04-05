package cn.wjhub.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类描述：
 *
 * @ClassName TraverseFolders
 * @Description 遍历文件夹
 * @Author 张文军
 * @Date 2021/4/6 1:53
 * @Version 1.0
 */
@Slf4j
public class TraverseFolders {

    public static void main(String[] args) throws IOException {
        traverseFolders(Paths.get("src\\main\\resources"));
    }
    private static void traverseFolders(Path path) throws IOException {
        AtomicInteger dirsCount = new AtomicInteger(0);
        AtomicInteger FilesCount = new AtomicInteger(0);
        Path tree = Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                log.info("dir---->{}",dir);
                dirsCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                log.info("file---->{}",file);
                FilesCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        log.info("dirsCount:{}",dirsCount.get());
        log.info("FilesCount:{}",FilesCount.get());
        log.info(tree.toString());
    }
}
