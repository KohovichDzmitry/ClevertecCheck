package ru.clevertec.check.util;

import java.util.Arrays;
import java.util.Iterator;

public class CustomArrayList<E> implements CustomList<E> {

    private int size;
    private E[] elementData;

    @Override
    public Iterator<E> getIterator() {
        return null;
    }

    @Override
    public void setMaxSize(int i) {

    }

    @Override
    public void add(E element) {

    }

    @Override
    public void addAll(CustomList<? extends E> customList) {

    }

    @Override
    public E set(int index, Object element) {
        return null;
    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int find(E element) {
        return 0;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void trim() {

    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
