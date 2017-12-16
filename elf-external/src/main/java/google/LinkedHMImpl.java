/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author bnevins
 */
public class LinkedHMImpl<K, V>   implements Map<K, V> {
    private static final int NUM_BUCKETS = 13;
    private final List<Entry<K,V>>[] buckets;
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        LinkedHMImpl<String, Integer> map = new LinkedHMImpl<>();
        map.put("aaaaa", 2);
        System.out.println(map.dump());
        map.put("bbbbb", 4);
        System.out.println(map.dump());
        map.put("ccccc", 6);
        
        for(int i = 1; i <= 500; i++) {
            String k = "qqqq" + random.nextInt();
            map.put("qqqq" + random.nextInt(), 99);
        }
        System.out.println(map.dump());
    }
    
    public LinkedHMImpl() {
        buckets = new List [NUM_BUCKETS];
    }
   
    @Override
    public V put(K key, V value) {
        int hash = key.hashCode();
        
        if(hash < 0)
            hash *= -1;
        hash %= NUM_BUCKETS;

        // lazy init
        if (buckets[hash] == null) {
            buckets[hash] = new LinkedList<>();
        }

        for (Entry<K,V> entry : buckets[hash]) {
            if (entry.key.equals(key)) {
                V retval = entry.value;
                entry.value = value;
                return retval;
            }
        }

        buckets[hash].add(new Entry<>(key, value));
        return null;
    }

    public String dump() {
        StringBuilder sb = new StringBuilder();
                
        for(int i = 0; i < NUM_BUCKETS; i++) {
            List<Entry<K,V>> list = buckets[i];
            sb.append("Bucket Number :" + i + "  ");
            
            if(list != null)
                for(Entry<K,V> e : list) {
                    sb.append(e.key).append("=").append(e.value);
                }
            sb.append('\n');
        }
        return sb.toString();
    }
    
    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public V get(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static class Entry<K, V> {
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        private K key;
        private V value;
    }
}