package ru.clevertec.check.custom;

public interface CustomIterator<E> {

    boolean hasNext();

    E next();

    void remove();

    void addBefore(E element);

    void addAfter(E element);
}
