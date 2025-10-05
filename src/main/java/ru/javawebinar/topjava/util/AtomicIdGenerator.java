package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(1);

    public static int nextId() {
        return counter.getAndIncrement();
    }
}
