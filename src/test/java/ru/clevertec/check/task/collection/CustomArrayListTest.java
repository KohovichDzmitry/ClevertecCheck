package ru.clevertec.check.task.collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.clevertec.check.util.CustomArrayList;
import ru.clevertec.check.util.CustomIterator;
import ru.clevertec.check.util.CustomList;

public class CustomArrayListTest {

    private CustomList<String> list;

    @Before
    public void init() {
        list = new CustomArrayList<>();
        list.add("Hello");
        list.add(" beautiful ");
        list.add("World!");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void checkMaxSize(){
        list = new CustomArrayList<>();
        list.setMaxSize(3);
        list.add("Hello");
        list.add(" beautiful ");
        list.add("World!");
        list.add(" and ");
        list.add("my ");
        list.add("friends");
    }

    @Test
    public void checkMaxSize2(){
        list = new CustomArrayList<>();
        list.setMaxSize(3);
        list.add("Hello");
        list.add(" beautiful ");
        list.add("World!");
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void checkMaxSize3(){
        list = new CustomArrayList<>();
        list.add("Hello");
        list.add(" beautiful ");
        list.add("World!");
        list.add(" and ");
        list.add("my ");
        list.add("friends");
        list.setMaxSize(3);
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void checkAdd(){
        list = new CustomArrayList<>();
        list.add(null);
        list.add("Hello");
        Assert.assertEquals(2, list.size());
        Assert.assertNull(list.get(0));
        Assert.assertEquals("Hello", list.get(1));
    }

    @Test
    public void checkAddWithIndex(){
        list = new CustomArrayList<>();
        list.add(null);
        list.add("Hello");
        list.add(" friend");
        list.add(2, " my");
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(" my", list.get(2));
    }

    @Test
    public void checkAddAllCollection(){
        CustomList<String> newList = new CustomArrayList<>();
        newList.add(" friend");
        newList.add(null);
        list.addAll(newList);
        Assert.assertEquals(5, list.size());
        Assert.assertNull(list.get(4));
        Assert.assertEquals("Hello", list.get(0));
    }

    @Test
    public void checkSet(){
        Assert.assertEquals("Hello", list.set(0, "Wow"));
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("Wow", list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSetByIncorrectIndex() {
        list.set(5, "Bill");
    }

    @Test
    public void checkRemove(){
        Assert.assertEquals(" beautiful ", list.remove(1));
        Assert.assertEquals(2, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void checkRemoveByIncorrectIndex() {
        list.remove(5);
    }

    @Test
    public void checkRemoveAndGet(){
        list.remove(1);
        Assert.assertEquals("World!", list.get(1));
    }

    @Test
    public void checkClear() {
        list.clear();
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void checkFind() {
        list.add("program");
        list.add(null);
        Assert.assertEquals(3, list.find("program"));
        Assert.assertEquals(4, list.find(null));
        Assert.assertEquals(-1, list.find("empty"));
        Assert.assertEquals(5, list.size());
    }

    @Test
    public void checkGet() {
        Assert.assertEquals("Hello", list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void checkGetByIncorrectIndex() {
        list.get(5);
    }

    @Test
    public void checkToArray() {
        Object[] array = list.toArray();
        Assert.assertEquals(3, array.length);
        Assert.assertEquals("Hello", array[0]);
        Assert.assertEquals("World!", array[2]);
    }

    @Test
    public void checkSize() {
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void checkTrim() {
        list.add(null);
        list.add(null);
        list.add("friend");
        list.add(null);
        list.trim();
        Assert.assertEquals(4, list.size());
        Assert.assertEquals("Hello", list.get(0));
        Assert.assertEquals("World!", list.get(2));
        Assert.assertEquals("friend", list.get(3));
    }

    @Test
    public void checkIteratorNext() {
        Assert.assertEquals("Hello", list.getIterator().next());
        Assert.assertEquals("Hello", list.getIterator().next());
    }

    @Test
    public void checkIteratorHasNext() {
        list = new CustomArrayList<>();
        Assert.assertFalse(list.getIterator().hasNext());
    }

    @Test
    public void checkIteratorNextAndHasNext() {
        CustomIterator<String> iterator = list.getIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Hello", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(" beautiful ", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("World!", iterator.next());
        Assert.assertFalse(iterator.hasNext());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void checkIteratorRemove() {
        CustomIterator<String> iterator = list.getIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Hello", iterator.next());
        iterator.remove();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(" beautiful ", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("World!", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void checkIteratorAddBefore() {
        CustomIterator<String> iterator = list.getIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Hello", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(" beautiful ", iterator.next());
        iterator.addBefore(" perfect and ");
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("World!", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void checkIteratorAndAddAfter() {
        CustomIterator<String> iterator = list.getIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Hello", iterator.next());
        iterator.addAfter(" perfect and ");
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(" beautiful ", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("World!", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}
