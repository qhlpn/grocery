package com.example.io;

import java.io.*;

public class IOoperate {

    // 输入到内存
    // 输出到外部

    // two abstract class
    // 字节流：InputStream / OutputStream
    // 字符流：Reader / Writer


    public static void main(String[] args) throws IOException {
//        file();
        inputOutputStream();
    }


    private static void file() throws IOException {

        String var1 = "C:\\Users\\74268\\Desktop\\demo.txt";
        File file = new File(var1);
        System.out.println(file.createNewFile());
        System.out.println(file.isFile());
        System.out.println(file.delete());

        String var2 = "C:\\Users\\74268\\Desktop";
        File dire = new File(var2);
        System.out.println(dire.isDirectory());
        File[] files = dire.listFiles((dir, name) -> true);
        for (File var3 : files) {
            System.out.println(var3.getAbsolutePath());
        }

    }


    private static void inputOutputStream() throws IOException {

        // abstract -> InputStream  OutputStream

        String path = "C:\\Users\\74268\\Desktop\\demo.txt";

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

    }

}
