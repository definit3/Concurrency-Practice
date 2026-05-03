package com.definit3.concurrency.lrucache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache {
    private Lock lock = new ReentrantLock(true);
    class Node {
        Node next;
        Node prev;
        int value;
        int key;
        Node(int key, int value, Node next, Node prev) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    int capacity = 0;
    int currentSize = 0;
    Node head = null;
    Node tail = null;
    Map<Integer, Node> map = new HashMap<>();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        head = new Node(-1, -1, null, null);
        tail = new Node(-1, -1, null, head);
        head.next = tail;
    }

    private int remove(int key) {
        Node node = map.get(key);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        map.remove(key);
        int value = node.value;
        node = null;
        return value;
    }

    private void add(Integer key, Integer value) {
        Node node = new Node(key, value, head.next, head);
        head.next.prev = node;
        head.next = node;
        map.put(key, node);
    }

    public int get(int key) {
        try {
            lock.lock();
            if (!map.containsKey(key)) return -1;
            int value = remove(key);
            add(key, value);
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void put(int key, int value) {
        try {
            lock.lock();
            if (map.containsKey(key)) {
                remove(key);
                add(key, value);
            } else if (currentSize < capacity) {
                add(key, value);
                currentSize++;
            } else {
                remove(tail.prev.key);
                add(key, value);
            }
        } finally {
            lock.unlock();
        }
    }
}
