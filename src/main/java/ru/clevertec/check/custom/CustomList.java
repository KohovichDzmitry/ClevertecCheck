package ru.clevertec.check.custom;

import java.util.stream.Stream;

public interface CustomList<E> {

    CustomIterator<E> getIterator();

    void setMaxSize(int index);

    void add(E element);

    void add(int index, E element);

    void addAll(CustomList<? extends E> customList);

    @SuppressWarnings("unchecked")
    static <E> CustomList<E> toCustomList(Object[] array) {
        return new CustomArrayList<>((E[]) array);
    }

    E set(int index, E element);

    E remove(int index);

    void clear();

    int find(E element);

    E get(int index);

    Object[] toArray();

    int size();

    void trim();

    Stream<E> stream();
}
