package ru.clevertec.check.task.collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.util.CustomArrayList;
import ru.clevertec.check.util.CustomIterator;
import ru.clevertec.check.util.CustomList;

public class CustomArrayListTest {

    static CustomList<String> list;

    @BeforeEach
    void generateCustomListTest() {
        list = new CustomArrayList<>();
        list.add("Hello");
        list.add(" beautiful ");
        list.add("World!");
    }

    @AfterEach
    void clearCustomListTest() {
        list.clear();
    }

    @Test
    void checkMaxSizeTest() {
        CustomList<String> list2 = new CustomArrayList<>();
        list2.setMaxSize(3);
        list2.add("Hello");
        list2.add(" beautiful ");
        list2.add("World!");
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> list2.add(" and "));
    }

    @Test
    void checkMaxSize2Test() {
        CustomList<String> list2 = new CustomArrayList<>();
        list2.setMaxSize(3);
        list2.add("Hello");
        list2.add(" beautiful ");
        list2.add("World!");
        Assertions.assertEquals(3, list2.size());
    }

    @Test
    void checkMaxSize3Test() {
        CustomList<String> list2 = new CustomArrayList<>();
        list2.add("Hello");
        list2.add(" beautiful ");
        list2.add("World!");
        list2.add(" and ");
        list2.add("my ");
        list2.add("friends");
        list2.setMaxSize(3);
        Assertions.assertEquals(3, list2.size());
    }

    @Test
    void checkAddTest() {
        CustomList<String> list2 = new CustomArrayList<>();
        list2.add(null);
        list2.add("Hello");
        Assertions.assertEquals(2, list2.size());
        Assertions.assertNull(list2.get(0));
        Assertions.assertEquals("Hello", list2.get(1));
    }

    @Test
    void checkAddWithIndexTest() {
        CustomList<String> list2 = new CustomArrayList<>();
        list2.add(null);
        list2.add("Hello");
        list2.add(" friend");
        list2.add(2, " my");
        Assertions.assertEquals(4, list2.size());
        Assertions.assertEquals(" my", list2.get(2));
    }

    @Test
    void checkAddAllCollectionTest() {
        CustomList<String> newList = new CustomArrayList<>();
        newList.add(" friend");
        newList.add(null);
        list.addAll(newList);
        Assertions.assertEquals(5, list.size());
        Assertions.assertNull(list.get(4));
        Assertions.assertEquals("Hello", list.get(0));
    }

    @Test
    void checkSetTest() {
        Assertions.assertEquals("Hello", list.set(0, "Wow"));
        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals("Wow", list.get(0));
    }

    @Test
    void checkSetByIncorrectIndexTest() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.set(5, "Bill"));
    }

    @Test
    void checkRemoveTest() {
        Assertions.assertEquals(" beautiful ", list.remove(1));
        Assertions.assertEquals(2, list.size());
    }

    @Test
    void checkRemoveByIncorrectIndexTest() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5));
    }

    @Test
    void checkRemoveAndGetTest() {
        list.remove(1);
        Assertions.assertEquals("World!", list.get(1));
    }

    @Test
    void checkClearTest() {
        list.clear();
        Assertions.assertEquals(0, list.size());
    }

    @Test
    void checkFindTest() {
        list.add("program");
        list.add(null);
        Assertions.assertEquals(3, list.find("program"));
        Assertions.assertEquals(4, list.find(null));
        Assertions.assertEquals(-1, list.find("empty"));
        Assertions.assertEquals(5, list.size());
    }

    @Test
    void checkGetTest() {
        Assertions.assertEquals("Hello", list.get(0));
    }

    @Test
    void checkGetByIncorrectIndexTest() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
    }

    @Test
    void checkToArrayTest() {
        Object[] array = list.toArray();
        Assertions.assertEquals(3, array.length);
        Assertions.assertEquals("Hello", array[0]);
        Assertions.assertEquals("World!", array[2]);
    }

    @Test
    void checkSizeTest() {
        Assertions.assertEquals(3, list.size());
    }

    @Test
    void checkTrimTest() {
        list.add(null);
        list.add(null);
        list.add("friend");
        list.add(null);
        list.trim();
        Assertions.assertEquals(4, list.size());
        Assertions.assertEquals("Hello", list.get(0));
        Assertions.assertEquals("World!", list.get(2));
        Assertions.assertEquals("friend", list.get(3));
    }

    @Test
    void checkIteratorNextTest() {
        Assertions.assertEquals("Hello", list.getIterator().next());
        Assertions.assertEquals("Hello", list.getIterator().next());
    }

    @Test
    void checkIteratorHasNextTest() {
        list = new CustomArrayList<>();
        Assertions.assertFalse(list.getIterator().hasNext());
    }

    @Test
    void checkIteratorNextAndHasNextTest() {
        CustomIterator<String> iterator = list.getIterator();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("Hello", iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(" beautiful ", iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("World!", iterator.next());
        Assertions.assertFalse(iterator.hasNext());
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    void checkIteratorRemoveTest() {
        CustomIterator<String> iterator = list.getIterator();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("Hello", iterator.next());
        iterator.remove();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(" beautiful ", iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("World!", iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    void checkIteratorAddBeforeTest() {
        CustomIterator<String> iterator = list.getIterator();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("Hello", iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(" beautiful ", iterator.next());
        iterator.addBefore(" perfect and ");
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("World!", iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    void checkIteratorAndAddAfterTest() {
        CustomIterator<String> iterator = list.getIterator();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("Hello", iterator.next());
        iterator.addAfter(" perfect and ");
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(" beautiful ", iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("World!", iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }
}
