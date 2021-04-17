package org.example.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author QiuHongLong
 */
@Slf4j
public class Operate {

    // bio = stream + buffer
    // nio = channel + buffer + selector


    public static void main(String[] args) throws Exception {

        ThreadPoolExecutor pool = new ThreadPoolExecutor(10,
                10,
                10,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.DiscardOldestPolicy());

//        demo1();
//        demo2();
//        demo3();
        pool.submit(() -> demo4());
        Thread.sleep(1000);
        pool.submit(() -> demosocketclient());

    }

    /**
     * 使用 FileChannel 和 ByteBuffer 来读取文件内容
     */
    private static void demo1() {
        try (RandomAccessFile raf = new RandomAccessFile(".\\demo.text", "rw")) {
            FileChannel fc = raf.getChannel();
            ByteBuffer bf = ByteBuffer.allocate(10);
            while (true) {
                int len = fc.read(bf);
                log.info("读取字节数：{}", len);
                if (len == -1) break;
                bf.flip();
                while (bf.hasRemaining()) log.info("{}", (char) bf.get());
                bf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理粘包
     */
    private static void demo2() {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\nhaha!\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source) {
        source.flip();
        int oldLimit = source.limit();
        for (int i = 0; i < oldLimit; i++) {
            if ('\n' == source.get(i)) {
                System.out.println(source.position());
                ByteBuffer target = ByteBuffer.allocate(i + 1 - source.position());
                source.limit(i + 1);
                target.put(source);
                source.limit(oldLimit);
                System.out.println(byteBufferToString(target));
            }
        }
        source.compact();
    }

    private static String byteBufferToString(ByteBuffer buffer) {
        buffer.flip();
        System.out.println("_____" + buffer.limit());
        byte[] bytes = new byte[buffer.limit()];
        for (int i = 0; i < buffer.limit(); i++) {
            bytes[i] = buffer.get(i);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }


    /**
     * FileChannel 零拷贝
     */
    private static void demo3() {
        String from = "from.txt";
        String to = "to.txt";
        try (FileChannel f = new FileInputStream(from).getChannel();
             FileChannel t = new FileOutputStream(to).getChannel()) {
            f.transferTo(0, f.size(), t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * SocketChannel 阻塞
     */
    private static void demo4() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(32);
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress(8080));
            SocketChannel sc;
            while (true) {
                sc = ssc.accept();
                break;
            }
            while (true) {
                sc.read(buffer);
                System.out.println(byteBufferToString(buffer));
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * socket 客户端
     */
    private static void demosocketclient() {
        ByteBuffer source = ByteBuffer.allocate(32);
        try {
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("localhost", 8080));
            source.put("hello world".getBytes());
            sc.write(source);
            source.clear();
            source.put("so slow".getBytes());
            sc.write(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
