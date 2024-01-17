package org.aimrobot.server4j.framework;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public class LRUCache<K, V> {

    public static class Entry<K, V> {

        public Entry<K, V> prev;
        public Entry<K, V> next;
        public K key;
        public V val;

        public Entry(){

        }

        public Entry(K key, V val) {
            this.key = key;
            this.val = val;
        }

    }

    private final Map<K, Entry<K, V>> cacheMap;
    private final Entry<K, V> head;
    private final Entry<K, V> tail;

    private final int capacity;
    private int size;

    public LRUCache(int capacity){
        this.capacity = capacity;
        this.size = 0;

        this.cacheMap = new HashMap<>(capacity);

        this.head = new Entry<>();
        this.tail = new Entry<>();

        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    public void put(K key, V val){
        Entry<K, V> entry = this.cacheMap.get(key);

        if(entry == null){
            entry = new Entry<>(key, val);

            if(this.size >= this.capacity){
                Entry<K, V> tail = removeTail();
                this.cacheMap.remove(tail.key);
                this.size--;
            }

            this.cacheMap.put(key, entry);
            this.size++;

        }else{
            entry.val = val;
        }

        moveToHead(entry);

    }

    public V get(K key){
        Entry<K, V> entry = this.cacheMap.get(key);

        if(entry != null){
            moveToHead(entry);
        }else{
            return null;
        }

        return entry.val;
    }

    public Collection<Map.Entry<K, Entry<K,V>>> entrySet(){
        return this.cacheMap.entrySet();
    }

    public Collection<Entry<K,V>> values(){
        return this.cacheMap.values();
    }

    public Set<K> keySet(){
        return this.cacheMap.keySet();
    }

    private Entry<K, V> removeTail(){
        Entry<K, V> entry = this.tail.prev;
        entry.prev.next = this.tail;
        this.tail.prev = entry.prev;

        entry.next = null;
        entry.prev = null;

        return entry;
    }

    private void moveToHead(Entry<K, V> entry){
        if(entry.next != null) entry.next.prev = entry.prev;
        if(entry.prev != null) entry.prev.next = entry.next;

        entry.prev = this.head;
        entry.next = this.head.next;

        this.head.next.prev = entry;
        this.head.next = entry;
    }

}

