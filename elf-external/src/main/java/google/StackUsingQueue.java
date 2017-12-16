package google;

import java.util.*;

/**
 * Given only a Q -- use it to implement a stack
 * No fair using fancy methods of Queue interface.  Do it the hard way!
 * @author wnevins
 */
public class StackUsingQueue {
    public static void main(String[] args) {
        StackUsingQueue sq = new StackUsingQueue();

        sq.push("A");
        sq.push("B");
        sq.push("C");
        sq.push("D");
        sq.push("E");

        System.out.println("Expecting to see EDCBA: " + sq.pop() +sq.pop() +sq.pop() +sq.pop() +sq.pop() );
    }

    SimpleQueue q = new SimpleQueue();

    public final void push(String s) {
        q.enqueue(s);
    }

    public final String pop() {
        if(q.isEmpty())
            throw new RuntimeException("Empty!");

        SimpleQueue old = q;
        q = new SimpleQueue();

        while(true) {
            String pop = old.dequeue();

            if(old.isEmpty())
                return pop;

            q.enqueue(pop);
        }
    }


    static class SimpleQueue {
        LinkedList<String> internalQ = new LinkedList<String>();

        private void enqueue(String s) {
            internalQ.add(s);
        }

        private String dequeue() {
            return internalQ.remove();
        }

        private boolean isEmpty() {
            return internalQ.isEmpty();
        }
    }
}
