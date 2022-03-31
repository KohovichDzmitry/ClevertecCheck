package ru.clevertec.check.util;

public interface CustomIterator<E> {

    boolean hasNext();

    E next();

    void remove();

    void addBefore(E element);

    void addAfter(E element);

}
