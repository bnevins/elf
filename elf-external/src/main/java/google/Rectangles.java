package google;

/**
 *
 * @author wnevins
 */
public class Rectangles {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Rect r1 = new Rect(new Point(0, 10), new Point(10, 0));
        Rect r2 = new Rect(new Point(0, 20), new Point(10, 10));
        Rect r3 = new Rect(new Point(0, 11), new Point(10, 0));
        Rect r4 = new Rect(new Point(0, 20), new Point(10, 10));
        Rect r5 = new Rect(new Point(10, 1000), new Point(1000, 10));
        Rect r6 = new Rect(new Point(0, 10), new Point(10, 0));
        Rect r7 = new Rect(new Point(9, 1000), new Point(1000, 9));
        Rect r8 = new Rect(new Point(0, 10), new Point(10, 0));
        Rectangles r = new Rectangles();
        System.out.println(r.check(r1, r2));
        System.out.println(r.check(r3, r4));
        System.out.println(r.check(r5, r6));
        System.out.println(r.check(r7, r8));

        System.out.println(r.check2(r1, r2));
        System.out.println(r.check2(r3, r4));
        System.out.println(r.check2(r5, r6));
        System.out.println(r.check2(r7, r8));
        /*
        Rect r2 = new Rect(new Point(0, 1), new Point(1,0));
        Rect r2 = new Rect(new Point(0, 1), new Point(1,0));
        Rect r2 = new Rect(new Point(0, 1), new Point(1,0));
         *
         */
    }

    boolean check(Rect r1, Rect r2) {
        //Order by X

        if (r2.ul.x < r1.ul.x) {
            Rect temp = r1;
            r1 = r2;
            r2 = temp;
        }

        // off to the right.  Can't intersect
        if (r2.ul.x > r1.lr.x)
            return false;

        // now we know the x's intersect.  What about y?
        // the "top" rectangle will be r1

        if (r1.lr.y < r2.ul.y) {
            Rect temp = r1;
            r1 = r2;
            r2 = temp;
        }

        if (r2.ul.y <= r1.lr.y)
            return false;
        return true;
    }

    boolean check2(Rect r1, Rect r2) {
        return intersects(r1.ul.x, r1.lr.x, r2.ul.x, r2.lr.x) &&
                intersects(r1.lr.y, r1.ul.y, r2.lr.y, r2.ul.y);
    }

    boolean intersects(int x1, int x2, int x3, int x4) {
        if (x1 < x3)
            return x3 < x2;
        else
            return x1 < x4;
    }

    static class Point {
        int x, y;

        public Point(int xx, int yy) {

            x = xx;
            y = yy;
        }
    }

    static class Rect {
        Point ul, lr;

        public Rect(Point ull, Point lrr) {
            ul = ull;
            lr = lrr;
        }
    }
}
