package ru.atom.game.objects.base.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGen {
    private final ConcurrentLinkedQueue<Integer> deletedId = new ConcurrentLinkedQueue<Integer>();
    private AtomicInteger lastId = new AtomicInteger(0);
    private boolean checkDeleted;

    public IdGen() {
        this(true);
    }

    public IdGen(boolean checkDeleted) {
        this.checkDeleted = checkDeleted;
    }

    public Integer generateId() {
        if(checkDeleted) {
            Integer id = deletedId.poll();
            if (id != null)
                return id;
        }
        return lastId.getAndIncrement();
    }

    public void addDeletedId(Integer id) {
        if(checkDeleted)
            deletedId.add(id);
    }
}
