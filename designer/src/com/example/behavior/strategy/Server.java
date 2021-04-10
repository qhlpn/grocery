package com.example.behavior.strategy;

public class Server {


    public <T> void sort(T[] array, Service s) {

        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (s.compare(array[j], array[j + 1])) {
                    swap(array, j, j + 1);
                }
            }
        }

    }


    private <T> void swap(T[] array, int l , int r) {
        T temp = array[l];
        array[l] = array[r];
        array[r] = temp;
    }


}
