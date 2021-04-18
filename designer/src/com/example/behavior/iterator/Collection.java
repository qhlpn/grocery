package com.example.behavior.iterator;

import java.util.ArrayList;
import java.util.List;

public class Collection<E> implements Aggregate {

    private List<E> pojos = new ArrayList<>();

    public E get(int index) {
        return pojos.get(index);
    }

    public boolean add(E e) {
        return pojos.add(e);
    }

    public int size() {
        return pojos.size();
    }

    @Override
    public Iterator<E> createIterator() {
        return new Iter<>(this);
    }

    private static class Iter<E> implements Iterator<E> {

        private Collection<E> pojos;
        private int index;

        public Iter(Collection<E> pojos) {
            this.pojos = pojos;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return pojos.size() > index;
        }

        @Override
        public E next() {
            return pojos.get(index++);
        }

    }



}
