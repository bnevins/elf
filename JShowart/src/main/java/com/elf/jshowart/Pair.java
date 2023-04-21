/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

/**
 *
 * @author bnevins
 */
public class Pair<K, V> {

    private K key;
    private V value;

    public Pair() {
        key = null;
        value = null;
    }
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K aKey) {
        key = aKey;
    }

    public void setValue(V aValue) {
        value = aValue;
    }
    //@Override
    public boolean equals(Pair<K, V> p) {
        return getKey().equals(p.getKey()) &&
               getValue().equals(p.getValue());
    }
    public static void main(String[] args) {
        Pair<Double,String> pair = new Pair<>();
        pair.setKey(1.2);
        pair.setValue("foo");
        Pair<Double,String> pair2 = new Pair<>(1.3, "foo");
        boolean same = pair.equals(pair2);
        Pair<Double,String> pair3 = new Pair<>(1.2, "foo");
        same = pair.equals(pair3);
    }
}
