package com.elf.sandbox;

/**
 * ****************************************************************************
 * Compilation: javac CollisionSystem.java Execution: java CollisionSystem n (n
 * random particles) java CollisionSystem < input.txt (from a file)
 * Dependencies: StdDraw.java Particle.java MinPQ.java Data files:
 * https://algs4.cs.princeton.edu/61event/diffusion.txt
 * https://algs4.cs.princeton.edu/61event/diffusion2.txt
 * https://algs4.cs.princeton.edu/61event/diffusion3.txt
 * https://algs4.cs.princeton.edu/61event/brownian.txt
 * https://algs4.cs.princeton.edu/61event/brownian2.txt
 * https://algs4.cs.princeton.edu/61event/billiards5.txt
 * https://algs4.cs.princeton.edu/61event/pendulum.txt
 *
 * Creates n random particles and simulates their motion according to the laws
 * of elastic collisions.
 *
 *****************************************************************************
 */
import com.elf.stdlib.StdDraw;
import com.elf.stdlib.StdIn;
import com.elf.util.sort.MinPQ;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 * The {@code CollisionSystem} class represents a collection of particles moving
 * in the unit box, according to the laws of elastic collision. This event-based
 * simulation relies on a priority queue.
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/61event">Section 6.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class CollisionSystem {

    private static final double HZ = 0.5;    // number of redraw events per clock tick
    private static final boolean STEPMODE = false;
    private MinPQ<Event> pq;          // the priority queue
    private double t = 0.0;          // simulation clock time
    private Particle[] particles;     // the array of particles
    private int msecPause = 20;

    /**
     * Initializes a system with the specified collection of particles. The
     * individual particles will be mutated during the simulation.
     *
     * @param particles the array of particles
     */
    public CollisionSystem(Particle[] particles) {
        this.particles = particles.clone();   // defensive copy
    }

    // updates priority queue with all new events for particle a
    private void predict(Particle a, double limit) {
        if (a == null) {
            return;
        }

        // particle-particle collisions
        for (int i = 0; i < particles.length; i++) {
            double dt = a.timeToHit(particles[i]);
            if (t + dt <= limit) {
                pq.insert(new Event(t + dt, a, particles[i]));
            }
        }

        // particle-wall collisions
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        if (t + dtX <= limit) {
            pq.insert(new Event(t + dtX, a, null));
        }
        if (t + dtY <= limit) {
            pq.insert(new Event(t + dtY, null, a));
        }
    }

    // redraw all particles
    private void redraw(double limit) {
        StdDraw.clear();
        for (int i = 0; i < particles.length; i++) {
            particles[i].draw();
        }
        StdDraw.show();
        StdDraw.pause(msecPause);
        if (t < limit) {
            pq.insert(new Event(t + 1.0 / HZ, null, null));
        }
    }

    /**
     * Simulates the system of particles for the specified amount of time.
     *
     * @param limit the amount of time
     */
    public void simulate(double limit) {

        // initialize PQ with collision events and redraw event
        pq = new MinPQ<Event>();
        for (int i = 0; i < particles.length; i++) {
            predict(particles[i], limit);
        }
        pq.insert(new Event(0, null, null));        // redraw event

        // the main event-driven simulation loop
        while (!pq.isEmpty()) {

            // get impending event, discard if invalidated
            Event e = pq.delMin();
            if (!e.isValid()) {
                continue;
            }
            Particle a = e.a;
            Particle b = e.b;

            // physical collision, so update positions, and then simulation clock
            for (int i = 0; i < particles.length; i++) {
                particles[i].move(e.time - t);
            }
            t = e.time;
            System.out.println("TIME: " + t);

            // process event
            if (a != null && b != null) {
                if (STEPMODE) {
                    // kill time...
                    getFromKB();
                }
                String s = dump(a, b, t);

                if (s != null) {
                    System.out.println(s);
                }

                a.bounceOff(b);              // particle-particle collision
            } else if (a != null && b == null) {
                a.bounceOffVerticalWall();   // particle-wall collision
            } else if (a == null && b != null) {
                b.bounceOffHorizontalWall(); // particle-wall collision
            } else if (a == null && b == null) {
                redraw(limit);               // redraw event
            }
            // update the priority queue with new collisions involving a or b
            predict(a, limit);
            predict(b, limit);
        }
    }

    private static String dump(Particle a, Particle b, double time) {
        // filter out noise...
        double a_e = a.kineticEnergy();
        double b_e = b.kineticEnergy();
        double total_e = a_e + b_e;

        if (total_e < 25.0e-10) {
            return null;
        }

        return String.format("Time: %.2f\nTHIS: %s\nTHAT: %s\n", 
                time, a.toStringAbridged(), b.toStringAbridged());
    }

    /**
     * *************************************************************************
     * An event during a particle collision simulation. Each event contains the
     * time at which it will occur (assuming no supervening actions) and the
     * particles a and b involved.
     *
     * - a and b both null: redraw event - a null, b not null: collision with
     * vertical wall - a not null, b null: collision with horizontal wall - a
     * and b both not null: binary collision between a and b
     *
     **************************************************************************
     */
    private static class Event implements Comparable<Event> {

        private final double time;         // time that event is scheduled to occur
        private final Particle a, b;       // particles involved in event, possibly null
        private final int countA, countB;  // collision counts at event creation

        // create a new event to occur at time t involving a and b
        public Event(double t, Particle a, Particle b) {
            this.time = t;
            this.a = a;
            this.b = b;
            if (a != null) {
                countA = a.count();
            } else {
                countA = -1;
            }
            if (b != null) {
                countB = b.count();
            } else {
                countB = -1;
            }
        }

        // compare times when two events will occur
        public int compareTo(Event that) {
            return Double.compare(this.time, that.time);
        }

        // has any collision occurred between when event was created and now?
        public boolean isValid() {
            if (a != null && a.count() != countA) {
                return false;
            }
            if (b != null && b.count() != countB) {
                return false;
            }
            return true;
        }

    }

    /**
     * Unit tests the {@code CollisionSystem} data type. Reads in the particle
     * collision system from a standard input (or generates {@code N} random
     * particles if a command-line integer is specified); simulates the system.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        StdDraw.setCanvasSize(900, 900);

        // enable double buffering
        StdDraw.enableDoubleBuffering();

        // the array of particles
        Particle[] particles;

        int num = 0;
        // create n random particles
        if (args.length == 1) {
            num = Integer.parseInt(args[0]);
            particles = new Particle[num];
            for (int i = 0; i < num; i++) {
                particles[i] = new Particle();
            }
            for (int i = 0; i < particles.length / 2; i++) {
                particles[i].setMass(25);
                particles[i].setColor(Color.BLACK);
            }
        } // or read from standard input
        else {
            String[] particleDescriptions;
            if (args.length == 2 && args[0].equalsIgnoreCase("-f")) {
                particleDescriptions = readParticleDescriptions(args[1]);
            } else // standard input
            {
                particleDescriptions = readParticleDescriptions();
            }

            particles = new Particle[particleDescriptions.length];
            int particleNum = 0;

            for (String desc : particleDescriptions) {
                Particle p = Particle.makeParticle(desc);
                if (p == null) {
                    throw new NullPointerException("Bad Input Line");
                }
                particles[particleNum++] = p;
            }
        }

        // create collision system and simulate
        CollisionSystem system = new CollisionSystem(particles);
        system.simulate(10000);
    }

    public static String[] readParticleDescriptions(String fname) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fname));
            return readParticleDescriptions(br);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CollisionSystem.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static String[] readParticleDescriptions() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return readParticleDescriptions(br);
    }

    private static String[] readParticleDescriptions(BufferedReader br) {
        try {
            ArrayList<String> ss = new ArrayList<>();

            for (String s = br.readLine(); s != null; s = br.readLine()) {
                // skip comments...
                if (s.length() < 9 || s.contains("/")) {
                    continue;
                }
                ss.add(s);
            }
            String[] values = new String[ss.size()];
            return ss.toArray(values);

        } catch (IOException ex) {
            Logger.getLogger(CollisionSystem.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static Particle makeHeavyFastParticle() {
//        rx = StdRandom.uniform(0.0, 1.0);
//        ry = StdRandom.uniform(0.0, 1.0);
//        vx = StdRandom.uniform(-0.005, 0.005);
//        vy = StdRandom.uniform(-0.005, 0.005);
//        radius = 0.02;
//        mass = pickMass();  //0.5;
//        color = pickColor();
        return new Particle(0.1, 0.1, .005, .005, .02, 5, Color.BLACK);
    }

    private static char getFromKB() {
        if (System.console() != null) {
            String s = System.console().readLine();

            if (s != null && s.length() > 0) {
                char c = s.charAt(0);

                if (c == 'q') {
                    System.exit(0);
                }

                return c;
            } else {
                return '.';
            }
        }
        Scanner scanner = new Scanner(System.in);
        return scanner.next().charAt(0);
    }
}
