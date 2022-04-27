package ru.clevertec.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, warmups = 0)
@Warmup(iterations = 0)
@Measurement(iterations = 2, time = 1, timeUnit = TimeUnit.MILLISECONDS)
public class BenchmarkList {
    private List<String> list;
    @Param({"100000"})
    private int quantity;
    @Param({"JAVA"})
    private String value;

    @Setup
    public void setUp() {
//        list = new ArrayList<>();
        list = new LinkedList<>();
        for (int i = 0; i < quantity; i++) {
            list.add(value);
        }
    }

    @Benchmark
    public void addElementToHead() {
        list.add(0, value);
    }

    @Benchmark
    public void addElementToMiddle() {
        list.add(list.size() / 2, value);
    }

    @Benchmark
    public void addElementToEnd() {
        list.add(value);
    }

    @Benchmark
    public void removeElement() {
        if (list.isEmpty()) {
            return;
        }
        list.remove(value);
    }

    @Benchmark
    public void removeFirstElement() {
        if (list.isEmpty()) {
            return;
        }
        list.remove(0);
    }

    @Benchmark
    public void removeMiddleElement() {
        if (list.isEmpty()) {
            return;
        }
        list.remove(list.size() / 2);
    }

    @Benchmark
    public void removeLastElement() {
        if (list.isEmpty()) {
            return;
        }
        list.remove(list.size() - 1);
    }

    @Benchmark
    public void getFirstElement() {
        list.get(0);
    }

    @Benchmark
    public void getMiddleElement() {
        list.get(list.size() / 2);
    }

    @Benchmark
    public void getLastElement() {
        list.get(list.size() - 1);
    }

    @Benchmark
    public void listContainsElement() {
        list.contains(value);
    }
}
