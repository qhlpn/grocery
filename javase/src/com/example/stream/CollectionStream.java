package com.example.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JDK集合流操作
 *
 * @author QiuHongLong
 */
public class CollectionStream {


    private static void transfer() {



    }


    private static void foreach() {


        List<String> list = Arrays.asList("1", "2", "3");

        for (String item : list) {
            System.out.println(item);
        }

        // 等效 list.stream().forEach
        list.forEach(System.out::println);

        // 并行
        list.parallelStream().forEach(System.out::println);
    }


    private static void filter() {

        List<String> list = Arrays.asList("1", "2", "3", "1");

        // 保留 true 的项
        list.stream().filter(item -> !"1".equals(item)).forEach(System.out::println);


    }


    private static void map() {

        List<String> list = Arrays.asList("1", "2", "3", "1");

        list.stream().map(item -> item = "3").forEach(System.out::println);

//        list.stream().forEach(item -> item = "3") -> is null

    }


    private static void flatMap() {

        List<String> a = Arrays.asList("1", "2", "3");
        List<String> b = Arrays.asList("1", "2", "3");

        // [[1, 2, 3], [1, 2, 3]]
        List<List<String>> c = Stream.of(a, b).collect(Collectors.toList());

        // flat 逐层平铺
        // item is List<String>
        List<String> d = Stream.of(a, b).flatMap(item -> item.stream()).collect(Collectors.toList());
        // [1, 2, 3, 4, 5]


    }


    public static void main(String[] args) {

//        foreach();
//        filter();
        map();
    }


}
