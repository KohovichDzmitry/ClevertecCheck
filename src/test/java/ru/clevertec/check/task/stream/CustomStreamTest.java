package ru.clevertec.check.task.stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.clevertec.check.util.CustomArrayList;
import ru.clevertec.check.util.CustomList;

import java.util.Optional;

public class CustomStreamTest {

    private CustomList<String> list;

    @Before
    public void init() {
        list = new CustomArrayList<>();
        list.add("Hello");
        list.add(" beautiful ");
        list.add("World!");
    }

    @Test
    public void testStream() {
        Assert.assertArrayEquals(new Integer[]{5, 11, 6}, list.stream()
                .map(String::length).toArray());

        Optional<String> stringOptional = list.stream()
                .filter(element -> element.length() < 3)
                .reduce((a, e) -> a + e);
        Assert.assertFalse(stringOptional.isPresent());
    }
}
