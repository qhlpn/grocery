package com.nowcoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        sc.nextLine();
        String[] ss;
        LinkedList<ArrayList<Integer>> ll = new LinkedList<>();
        ArrayList<Integer> l;
        for (int j = 0; j < num; j++) {
            sc.nextLine();
            ss = sc.nextLine().split(" ");
            l = new ArrayList<>();
            for (int i = 0; i < ss.length; ++i) {
                l.add(Integer.parseInt(ss[i]));
            }
            Collections.sort(l);
            ll.add(l);
        }
        while (ll.size() > 0) {
            l = ll.pollFirst();
            int totalTime = 0;
            if (l.size() >= 3) {
                totalTime= 0;
                for (int i = l.size() - 1; i > 2; i--) {
                    totalTime += l.get(i) + l.get(1);
                }
                totalTime += l.get(2);
            } else if (l.size() == 2) {
                totalTime = l.get(1);
            }

            System.out.println(totalTime);
        }
    }
}
