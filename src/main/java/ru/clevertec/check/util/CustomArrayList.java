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
    public void setMaxSize(int index) {
        if (index > 0) {
            maxSize = index;
            if (size > maxSize) {
                elementData = Arrays.copyOf(elementData, maxSize);
                size = maxSize;
            }
        } else {
            throw new IllegalArgumentException("Устанавливаемое максимальное количество элементов " +
                    "должно быть больше нуля, а не " + index);
        }
    }

    @Override
    public void add(E element) {
        int s = size;
        if (maxSize == 0 || size < maxSize) {
            elementData = Arrays.copyOf(elementData, s + 1);
            elementData[s] = element;
            size = s + 1;
        } else {
            throw new ArrayIndexOutOfBoundsException("Превышено установленное максимальное количество элементов");
        }
    }

    @Override
    public void addAll(CustomList<? extends E> customList) {
        Object[] a = customList.toArray();
        int s = size;
        if (maxSize == 0 || size + a.length < maxSize + 1) {
            elementData = Arrays.copyOf(elementData, s + a.length);
            System.arraycopy(a, 0, elementData, s, a.length);
            size = s + a.length;
        } else {
            throw new ArrayIndexOutOfBoundsException("Превышено установленное максимальное количество элементов");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        Objects.checkIndex(index, size);
        E oldValue = (E) elementData[index];
        int newSize = size - 1;
        if (newSize > index)
            System.arraycopy(elementData, index + 1, elementData, index, newSize - index);
        size = newSize;
        return oldValue;
    }

    @Override
    public void clear() {
        final Object[] es = elementData;
        for (int to = size, i = size = 0; i < to; i++)
            es[i] = null;
    }

    @Override
    public int find(E element) {
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
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
        for (int i = 0; i < size; i++) {
            if (elementData[i] == null) {
                remove(i);
            }
        }
    }

    @Override
    public Iterator<E> getIterator() {
        return null;
    }

//    public String toString() {
//        Iterator<E> it = getIterator();
//        if (! it.hasNext())
//            return "[]";
//
//        StringBuilder sb = new StringBuilder();
//        sb.append('[');
//        for (;;) {
//            E e = it.next();
//            sb.append(e == this ? "(this Collection)" : e);
//            if (! it.hasNext())
//                return sb.append(']').toString();
//            sb.append(',').append(' ');
//        }
//    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elementData[i]);
            if (i != size - 1)
                sb.append(',').append(' ');
        }
        return sb.append(']').toString();
    }
}
