package com.example.behavior.iterator;

public class Caller {

    public static void main(String[] args) {
        Collection<Integer> pojos = new Collection<>();
        pojos.add(5);
        pojos.add(7);
        pojos.add(3);
        pojos.add(5);
        Iterator<Integer> pojoIter = pojos.createIterator();
        while (pojoIter.hasNext()) {
            System.out.println(pojoIter.next());
        }
    }

}
