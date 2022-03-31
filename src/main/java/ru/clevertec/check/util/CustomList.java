package ru.clevertec.check.util;

public interface CustomList<E> {

    CustomIterator<E> getIterator();

    void setMaxSize(int index);

    void add(E element);

    void add(int index, E element);

    void addAll(CustomList<? extends E> customList);

    E set(int index, E element);

    E remove(int index);

    void clear();

    int find(E element);

    E get(int index);

    Object[] toArray();

    int size();

    void trim();
}
