package org.example.bio;

import java.io.*;

/**
 * @author QiuHongLong
 */
public class Operate {

    // 输入到内存
    // 输出到外部

    // abstract class
    // 字节流：InputStream / OutputStream
    // 字符流：Reader / Writer

    // 装饰者模式


    public static void main(String[] args) throws IOException {

        file();
//        inputOutputStream();
//        objectSerialStream();
//        readerWriter();
//        inputStreamToReaderAndOutputStreamToWriter();

    }


    private static void file() throws IOException {

        String var1 = ".\\demo.txt";
        File file = new File(var1);
        System.out.println(file.createNewFile());
        System.out.println(file.isFile());
        System.out.println(file.delete());

        String var2 = ".\\src";
        File dire = new File(var2);
        System.out.println(dire.isDirectory());
        File[] files = dire.listFiles((dir, name) -> true);
        for (File var3 : files) {
            System.out.println(var3.getAbsolutePath());
        }

    }


    private static void inputOutputStream() throws IOException {

        // abstract -> InputStream  OutputStream
        // impl -> FileInputStream  FileOutputStream

        String path = ".\\demo.txt";
        File file = new File(path);
        file.createNewFile();

        // impl -> FileOutputStream
        // append -> true / false
        try (OutputStream os = new FileOutputStream(path, true)) {
            // \n\rHello
            byte[] buffer = new byte[]{10, 13, 72, 101, 108, 108, 111};
            os.write(buffer);
        }

        // impl -> FileInputStream
        try (InputStream is = new FileInputStream(path)) {
            byte[] buffer = new byte[10];
            int number;
            StringBuilder sb = new StringBuilder();
            while ((number = is.read(buffer)) != -1) {
                for (int i = 0; i < number; i++) {
                    sb.append((char) buffer[i]);
                }
            }
            System.out.println(sb.toString());
        }

        file.delete();

    }


    /**
     * Java序列化机制将对象变为byte[]数组
     */
    public static class SerialObject implements Serializable {

        // 非必需字段，自动阻止不匹配的版本，以防抛出 InvalidClassException
        private static final long serialVersionUID = 1L;

        private int id = 10;

        // 反序列化时不会执行构造函数，因为是序列化对象（属性值），而不是类
        public SerialObject() {
            id = 10;
            System.out.println("~~~~~~~~~~~~~~~~~~");
        }

        public int getId() {
            return id;
        }

    }

    private static void objectSerialStream() throws IOException {

        // abstract -> InputStream  OutputStream
        // impl -> ObjectInputStream  ObjectOutputStream

        String path = ".\\demo.txt";
        File file = new File(path);
        file.createNewFile();

        SerialObject o1 = new SerialObject();
        System.out.println(o1.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(o1);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            Object o = ois.readObject();
            if (o instanceof SerialObject) {
                SerialObject o2 = (SerialObject) o;
                System.out.println(o2.getId());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        file.delete();

    }

    private static void readerWriter() throws IOException {

        // abstract -> Reader  Writer
        // impl -> FileReader FileWriter

        String path = ".\\demo.txt";
        File file = new File(path);
        file.createNewFile();

        try (FileWriter fw = new FileWriter(file)) {
            fw.write("hello word !!");
        }

        try (FileReader fr = new FileReader(file)) {
            char[] buffer = new char[1024];
            int number;
            while ((number = fr.read(buffer)) != -1) {
                System.out.println(String.valueOf(buffer, 0, number));
            }
        }
        file.deleteOnExit();
    }


    /**
     * 装饰模式
     * @throws IOException
     */
    private static void inputStreamToReaderAndOutputStreamToWriter() throws IOException {

        // Reader 实际上是基于 InputStream 构造
        // InputStreamReader 作为转换器，可将任何的 InputStream Impl 转换为 Reader Abstract

        // Writer 与 OutputStream 间关系同理

        String path = ".\\demo.txt";
        File file = new File(path);
        file.createNewFile();

        try (FileOutputStream fos = new FileOutputStream(file)) {
            try (Writer w = new OutputStreamWriter(fos)) {
                w.write("hello world !!!");
            }
        }

        try (FileInputStream fis = new FileInputStream(file)){
            try (Reader r = new InputStreamReader(fis)) {
                char[] buffer = new char[1024];
                int number;
                while ((number = r.read(buffer)) != -1) {
                    System.out.println(String.valueOf(buffer, 0, number));
                }
            }

        }

        file.delete();
    }


}
