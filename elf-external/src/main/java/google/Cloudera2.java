package google;
/**
 *
 * @author wnevins
 */
public class Cloudera2 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
interface Stack {
    public void push(int elem);
    public int pop();
    public boolean isEmpty();
    public int size();
}

public class StackImpl implements Stack {
    public void push(int elem) {}
    public int pop() { return 1; }
    public boolean isEmpty() { return false; }
    public int size() { return 2; }
}
/**
 Feeble class for implementing a Queue.  All we had to work with
 was stacks!
 */

public class Queue {
    public void enqueue(int elem) {
        reg.push(elem);
    }
    public int dequeue() throws IllegalAccessException {
        if(isEmpty())
                throw new IllegalAccessException("Empty Queue");

        if(rev.isEmpty())
            fillerup();

        return rev.pop();
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public int size() {
        return reg.size() + rev.size();
    }

    private void fillerup() {
        while(!reg.isEmpty())
            rev.push(reg.pop());
    }

    private Stack reg = new StackImpl();
    private Stack rev = new StackImpl();
    }
}
