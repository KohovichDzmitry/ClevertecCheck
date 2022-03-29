package ru.clevertec.check.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class CustomArrayList<E> implements CustomList<E> {

    private int size, maxSize;
    private Object[] elementData;

    public CustomArrayList() {
        this.elementData = new Object[]{};
    }

    @Override
    public Iterator<E> getIterator() {
        return null;
    }

    @Override
    public void setMaxSize(int index) {
        if (index > 0) {
            maxSize = index;
            Object[] a = elementData;
            elementData = Arrays.copyOf(a, maxSize);
        } else {
            throw new IllegalArgumentException("Устанавливаемое максимальное количество элементов " +
                    "должно быть больше нуля, а не " + index);
        }
    }

    @Override
    public void add(E element) {
        int s = size;
        if (maxSize == 0) {
            elementData = Arrays.copyOf(elementData, s + 1);
            elementData[s] = element;
            size = s + 1;
        }
        if (size < maxSize) {
            elementData[s] = element;
            size = s + 1;
        }
    }

    @Override
    public void addAll(CustomList<? extends E> customList) {

    }

    @Override
    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
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
        Objects.checkIndex(index, size);
        return (E) elementData[index];
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void trim() {

    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
