package com.example.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JDK集合流操作
 *
 * @author QiuHongLong
 */
public class CollectionStream {

    /**
     *
     * | 函数接口       | 抽象方法         | 功能                   | 参数   | 返回类型 |
     * | :------------- | :-------------- | :--------------------- | :----- | :------- |
     * | Predicate      | test(T t)       | 判断真假               | T      | boolean  |
     * | Consumer       | accept(T t)     | 消费消息               | T      | void     |
     * | Function       | R apply(T t)    | 将T映射为R（转换功能）  | T      | R        |
     * | Supplier       | T get()         | 生产消息               | None   | T        |
     * | UnaryOperator  | T apply(T t)    | 一元操作               | T      | T        |
     * | BinaryOperator | apply(T t, U u) | 二元操作               | (T，T) | (T)      |
     *
     *
     * | Stream流函数              | 功能                                                  | 底层       | 类型         |
     * | :------------------------ | :---------------------------------------------------- | :--------- | :----------- |
     * | s.collect                 | 流转换成集合，Collectors.toList() / toSet() / toMap() |            | Terminal     |
     * | s.filter                  | 过滤筛选                                              | Predicate  | Intermediate |
     * | s.map                     | 映射转换                                              | Function   | Intermediate |
     * | s.flatMap                 | 将多个Stream合并成一个Stream                          | Function   | Intermediate |
     * | s.max / min               | 流中最大和值最小值                                    | Comparator | Terminal     |
     * | s.count                   | 流元素个数                                            |            | Terminal     |
     * | s.reduce                  | 累计操作                                              |            | Terminal     |
     * | Collectors.partitioningBy | 将流拆分成两个集合                                    | Predicate  |              |
     * | Collectors.groupingBy     | 将流元素进行集合分组                                  | Function   |              |
     */



    public static void main(String[] args) {

//        transfer();
//        foreach();
//        filter();
//        map();
//        flatMap();
//        sorted();
//        distinct();
//        count();
//        skip_limit();
//        min_max();
//        anyMatch_allMatch();
//        findAny_findFirst();

    }

    private static void transfer() {

        List<String> var1 = Arrays.asList("1", "2", "3");

        // list -> stream
        // serial
        var1.stream();

        // parallel
        var1.parallelStream();

        // stream -> Collection
        var1.stream().collect(Collectors.toList());
        Stream.of("1", "2", "3").collect(Collectors.toList());

        // stream -> array
        var1.stream().toArray();


        List<String> var2 = Arrays.asList("4", "5", "6");

        Stream.of(var1, var2);

        Stream.concat(var1.stream(), var2.stream());

    }


    private static void foreach() {


        List<String> list = Arrays.asList("1", "2", "3");

        for (String item : list) {
            System.out.println(item);
        }

        // 等效 list.stream().forEach
        list.forEach(System.out::println);

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

        // list.stream().forEach(item -> item = "3") -> is null

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

    private static void sorted() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4);

        // (l, r) -> l - r
        // 升序，正数交换，非正不动
        list.stream().sorted(Comparator.comparingInt(l -> l)).forEach(System.out::println);

    }

    private static void distinct() {

        List<Integer> list = Arrays.asList(1, 1, 1, 1);

        list.stream().distinct().forEach(System.out::println);

    }

    private static void count() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4);

        System.out.println(list.stream().count());

    }


    private static void skip_limit() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4);

        list.stream().skip(1).limit(1).forEach(System.out::println);
        // 2

    }

    private static void min_max() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4);

        // max : 排序后的最末位
        Optional<Integer> var1 = list.stream().max(Comparator.comparingInt(l -> l));
        Optional<Integer> var2 = list.stream().max((l, r) -> r - l);

        System.out.println(var1.get());
        System.out.println(var2.get());

    }

    private static void anyMatch_allMatch() {

        List<String> list = Arrays.asList("10", "11", "12", "13");

        System.out.println(list.stream().anyMatch(item -> "10".equals(item)));
        // true

        System.out.println(list.stream().allMatch(item -> item.contains("1")));
        // true

    }

    private static void findAny_findFirst() {

        List<String> list = Arrays.asList("10", "11", "12", "13");

        Optional var1 = list.stream().findFirst();
        Optional var2 = list.stream().findAny();

        System.out.println(var1.get());
        System.out.println(var2.get());

    }




}
