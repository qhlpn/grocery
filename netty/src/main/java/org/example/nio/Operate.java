package org.example.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
//        pool.submit(() -> demo5());

        new BossEventLoop().register();
        while (true) {
            pool.submit(() -> demosocketclient());
            Thread.sleep(50);
        }


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


    /**
     * socket 客户端
     */
    private static void demosocketclient() {
        try {
            SocketChannel sc = SocketChannel.open();
            ByteBuffer source = ByteBuffer.allocate(32);
            sc.connect(new InetSocketAddress("localhost", 8888));
            source.put("flip before read\n".getBytes());
            source.flip();
            sc.write(source);
            source.clear();
            source.put("hello world\n".getBytes());
            source.flip();
            sc.write(source);
            sc.write(Charset.defaultCharset().encode("hello world\n"));
            sc.write(Charset.defaultCharset().encode("so slow\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class BossEventLoop implements Runnable {

        private Selector selector;
        private WorkerEventLoop[] workers;
        private volatile boolean start = false;
        private AtomicInteger index = new AtomicInteger();

        public void register() throws IOException {
            if (!start) {
                ServerSocketChannel ssc = ServerSocketChannel.open();
                ssc.bind(new InetSocketAddress(8888));
                ssc.configureBlocking(false);
                selector = Selector.open();
                log.info("boss selector is {}", selector.toString());
                SelectionKey sscKey = ssc.register(selector, SelectionKey.OP_ACCEPT, null);
                this.workers = initWorkers();
                new Thread(this, "boss").start();
                log.info("boss start ....");
                start = true;
            }
        }

        private WorkerEventLoop[] initWorkers() {
            WorkerEventLoop[] eventLoops = new WorkerEventLoop[Runtime.getRuntime().availableProcessors()];
            log.debug("available processors cnt: {}", Runtime.getRuntime().availableProcessors());
            for (int i = 0; i < eventLoops.length; i++) {
                eventLoops[i] = new WorkerEventLoop(i);
            }
            return eventLoops;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    int cnt = selector.select();
                    log.info("boss selectedkey cnt : {}", cnt);
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isAcceptable()) {
                            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);
                            log.debug("{} connected", sc.getRemoteAddress());
                            workers[index.getAndIncrement() % workers.length].register(sc);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    static class WorkerEventLoop implements Runnable {

        private Selector selector;
        private volatile boolean start = false;
        private int index;

        public WorkerEventLoop(int index) {
            this.index = index;
        }

        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                selector = Selector.open();
                log.info("worker-{} selector is {}", index, selector.toString());
                new Thread(this, "worker-" + index).start();
                log.info("worker-{} start ....", index);
                start = true;
            }
            try {
                selector.wakeup();
                sc.register(selector, SelectionKey.OP_READ, null);
                int cnt = selector.selectNow();
                log.info("worker-{} selectNow selectedkey cnt : {}", index, cnt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    int cnt = selector.select();
                    log.info("worker-{} select selectedkey cnt : {}", index, cnt);
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int read = sc.read(buffer);
                            try {
                                if (read == -1) {
                                    key.cancel();
                                    sc.close();
                                } else {
                                    log.info("{} message: ", sc.getRemoteAddress());
                                    System.out.println(byteBufferToString(buffer));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                key.cancel();
                                sc.close();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
