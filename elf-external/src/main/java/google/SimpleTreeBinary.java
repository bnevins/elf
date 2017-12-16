/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Create a binary tree
 *
 * @author bnevins
 */
public class SimpleTreeBinary {
    Node root = null;
    TreeMap map;

    public static void main(String[] args) {
        SimpleTreeBinary tree = new SimpleTreeBinary();
        tree.add("foooo");
        tree.add("goooo");
        tree.add("hoooo");
        tree.add("friend");
        tree.add("zoo");
        tree.add("ccc");
        tree.add("aaa");
        tree.preorder();
        tree.inorder();
        tree.postorder();
    }

    public SimpleTreeBinary() {
        root = null;
    }

    public void add(String value) {
        if (root == null) {
            setRoot(value);
            return;
        }

        Node parent = root;
        Node current = root;
        boolean right = true;

        while (current != null) {
            parent = current;
            int comp = value.compareTo(current.value);

            if (comp > 0) {
                right = true;
                current = current.right;
            }
            else if (comp < 0) {
                right = false;
                current = current.left;
            }
            else {
                current.value = value;
                return;
            }
        }
        if (right) {
            parent.right = new Node(value);
        }
        else {
            parent.left = new Node(value);
        }
    }

    private void preorder() {
        System.out.println("****   PREORDER ****");
        preorder(root);
    }

    private void preorder(Node node) {
        if (node == null) {
            return;
        }

        node.print();
        preorder(node.left);
        preorder(node.right);
    }

    private void inorder() {
        System.out.println("****   INORDER ****");
        inorder(root);
    }

    private void inorder(Node node) {
        if (node == null) {
            return;
        }

        inorder(node.left);
        node.print();
        inorder(node.right);
    }

    private void postorder() {
        System.out.println("****   postorder ****");
        postorder(root);
    }

    private void postorder(Node node) {
        if (node == null) {
            return;
        }

        postorder(node.left);
        postorder(node.right);
        node.print();
    }

    private void setRoot(String value) {
        root = new Node(value);
    }

    private static class Node {
        public Node(String v) {
            value = v;
        }
        String value;
        Node left;
        Node right;

        void print() {
            System.out.println(value);
        }
    }
}
