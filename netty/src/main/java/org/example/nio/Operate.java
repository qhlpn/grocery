package org.example.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
//        pool.submit(() -> demo4());


        pool.submit(() -> demo5());

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
        // 需要切换到 读模式 才可以 read
        // 注意 position limit
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        source.flip();
        split(source);
        source.put("w are you?\nhaha!\n".getBytes());
        source.flip();
        split(source);

        // Charset.defaultCharset().encode 返回的 buffer 已帮你切换到 读模式
        split(Charset.defaultCharset().encode("Hello,world\nI'm zhangsan\nHow are you?\nhaha!\n"));


    }

    private static void split(ByteBuffer source) {
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
            long size = f.size();
            for (long left = size; left > 0; ) {
                // transferTo 一次最多 2g
                left -= f.transferTo(size - left, left, t);
                // size = 5g
                // 0 5
                // 2 3
                // 4 1
            }
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
     * SocketChannel 非阻塞
     */
    private static void demo5() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(32);
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(8080));
            List<SocketChannel> channels = new ArrayList<>();
            while (true) {
                SocketChannel sc = ssc.accept();
                if (sc != null) {
                    sc.configureBlocking(false);
                    channels.add(sc);
                }
                for (SocketChannel channel : channels) {
                    int cnt = channel.read(buffer);
                    if (cnt > 0) {
                        System.out.println(byteBufferToString(buffer));
                        buffer.clear();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void demo6() {

    }


    /**
     * socket 客户端
     */
    private static void demosocketclient() {
        try {
            SocketChannel sc = SocketChannel.open();
            ByteBuffer source = ByteBuffer.allocate(32);
            sc.connect(new InetSocketAddress("localhost", 8080));
            source.put("flip before read".getBytes());
            source.flip();
            sc.write(source);
            source.clear();
            source.put("hello world".getBytes());
            source.flip();
            sc.write(source);
            sc.write(Charset.defaultCharset().encode("hello world"));
            sc.write(Charset.defaultCharset().encode("so slow"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
