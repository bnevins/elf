package com.elf.util.sort;

import java.util.Map;

/**
 *
 * @author bnevns
 * @param <Key>
 * @param <Value>
 */
public class BinaryTree<Key extends Comparable<Key>, Value> {
    private Node root;

    private class Node {
        Node left;
        Node right;
        Key key;
        Value value;
        int numNodes;

        Node(Key aKey, Value aValue, int n) {
            key = aKey;
            value = aValue;
            numNodes = n;
        }
    }

    public int size() {
        return size(root);
    }

    public int size(Node node) {
        if (node == null) {
            return 0;
        }
        return node.numNodes;
    }

    public void put(Key k, Value v) {
        root = put(root, k, v);
    }

    public Value get(Key k) {
        return get(root, k);
    }

    private Value get(Node node, Key k) {
        if (node == null) {
            return null;
        }
        int cmp = k.compareTo(node.key);

        if (cmp < 0) {
            System.out.println(node.key + "  <0");
            return get(node.left, k);
        } else if (cmp > 0) {
            System.out.println(node.key + "  >0");
            return get(node.right, k);
        } else {
            System.out.println(node.key + "  ==");

            return node.value;
        }
    }

    private Node put(Node node, Key k, Value v) {
        if (node == null) {
            return new Node(k, v, 1);
        }

        int cmp = k.compareTo(node.key);

        if (cmp < 0) {
            node.left = put(node.left, k, v);
        } else if (cmp > 0) {
            node.right = put(node.right, k, v);
        } else {
            node.value = v;
        }
        node.numNodes = size(node.left) + size(node.right) + 1;
        return node;
    }

    public static void main(String[] args) {
        BinaryTree<String, Integer> tree = new BinaryTree<>();
        tree.put("hello", 25);
        tree.put("goodbye", 5);
        tree.put("foo", 3);
        tree.put("foo", 3);
        tree.put("ccc", 1);
        tree.put("aaa", 1);
        tree.get("fooo");
    }
}
