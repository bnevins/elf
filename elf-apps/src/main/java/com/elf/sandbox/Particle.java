/** ****************************************************************************
 *  Compilation:  javac Particle.java
 *  Execution:    none
 *  Dependencies: StdDraw.java
 *
 *  A particle moving in the unit box with a given position, velocity,
 *  radius, and mass.
 *
 ***************************************************************************** */
package com.elf.sandbox;

import com.elf.algorithms.stdlib.*;
import com.elf.util.StringUtils;
import java.awt.Color;
import static java.awt.Color.*;

/**
 * The {@code Particle} class represents a particle moving in the unit box, with
 * a given position, velocity, radius, and mass. Methods are provided for moving
 * the particle and for predicting and resolvling elastic collisions with
 * vertical walls, horizontal walls, and other particles. This data type is
 * mutable because the position and velocity change.
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/61event">Section 6.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class Particle {

    private static final double INFINITY = Double.POSITIVE_INFINITY;

    private double rx, ry;        // position
    private double vx, vy;        // velocity
    private int count;            // number of collisions so far
    private double radius;  // radius
    private double mass;    // mass
    private Color color;    // color

    /**
     * Initializes a particle with the specified position, velocity, radius,
     * mass, and color.
     *
     * @param rx <em>x</em>-coordinate of position
     * @param ry <em>y</em>-coordinate of position
     * @param vx <em>x</em>-coordinate of velocity
     * @param vy <em>y</em>-coordinate of velocity
     * @param radius the radius
     * @param mass the mass
     * @param color the color
     */
    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
        this.vx = vx;
        this.vy = vy;
        this.rx = rx;
        this.ry = ry;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
    }

    public static Particle makeParticle(String line) {
        // FORMAT: 
        // rx, ry, vx, vy, radius, mass, red, green, blue
        double[] values = StringUtils.getDoubles(line);
        if (values == null) {
            return null;
        }
        if (values.length != 9) {
            throw new IllegalArgumentException(line + "  -->  Length expected: 9, got: " + values.length);
        }
        int index = 0;

        Particle p = new Particle(
                values[index++], values[index++], values[index++],
                values[index++], values[index++], values[index++],
                makeColor(values[index++], values[index++], values[index++]));
        return p;
    }

    /**
     * Initializes a particle with a random position and velocity. The position
     * is uniform in the unit box; the velocity in either direciton is chosen
     * uniformly at random.
     */
    public Particle() {
        rx = StdRandom.uniform(0.0, 1.0);
        ry = StdRandom.uniform(0.0, 1.0);
        vx = StdRandom.uniform(-0.005, 0.005);
        vy = StdRandom.uniform(-0.005, 0.005);
        radius = 0.02;
        mass = pickMass();  //0.5;
        color = pickColor();
    }
    static final Color[] colors = {BLACK, RED};
    //static final Color[] colors = {BLACK, RED, PINK, BLUE, GREEN, YELLOW, GRAY, DARK_GRAY, ORANGE, CYAN, MAGENTA};
    static final double[] masses = {0.1, 5.0};
    static int currColorIndex = 0;
    static int currMassIndex = 0;

//    public void setColor(Color color) {
//        this.color = color;
//    }
    Color pickColor() {
        return RED;
//        if (currColorIndex >= colors.length) {
//            currColorIndex = 0;
//        }
//
//        return colors[currColorIndex++];
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    double pickMass() {
        return 0.5;
//        if (currMassIndex >= masses.length) {
//            currMassIndex = 0;
//        }
//
//        return masses[currMassIndex++];
    }

    private static Color makeColor(double r, double g, double b) {

        // not perfect -- 
        if (r > 1) {
            r /= 255;
        }
        if (b > 1) {
            b /= 255;
        }
        if (g > 1) {
            g /= 255;
        }
        return new Color((float)r, (float)g, (float)b);
    }

    /**
     * Moves this particle in a straight line (based on its velocity) for the
     * specified amount of time.
     *
     * @param dt the amount of time
     */
    public void move(double dt) {
        rx += vx * dt;
        ry += vy * dt;
    }

    /**
     * Draws this particle to standard draw.
     */
    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(rx, ry, radius);
    }

    /**
     * Returns the number of collisions involving this particle with vertical
     * walls, horizontal walls, or other particles. This is equal to the number
     * of calls to {@link #bounceOff},
     * {@link #bounceOffVerticalWall}, and {@link #bounceOffHorizontalWall}.
     *
     * @return the number of collisions involving this particle with vertical
     * walls, horizontal walls, or other particles
     */
    public int count() {
        return count;
    }

    /**
     * Returns the amount of time for this particle to collide with the
     * specified particle, assuming no interening collisions.
     *
     * @param that the other particle
     * @return the amount of time for this particle to collide with the
     * specified particle, assuming no interening collisions;
     * {@code Double.POSITIVE_INFINITY} if the particles will not collide
     */
    public double timeToHit(Particle that) {
        if (this == that) {
            return INFINITY;
        }
        double dx = that.rx - this.rx;
        double dy = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        double dvdr = dx * dvx + dy * dvy;
        if (dvdr > 0) {
            return INFINITY;
        }
        double dvdv = dvx * dvx + dvy * dvy;
        if (dvdv == 0) {
            return INFINITY;
        }
        double drdr = dx * dx + dy * dy;
        double sigma = this.radius + that.radius;
        double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);
        // if (drdr < sigma*sigma) StdOut.println("overlapping particles");
        if (d < 0) {
            return INFINITY;
        }
        return -(dvdr + Math.sqrt(d)) / dvdv;
    }

    /**
     * Returns the amount of time for this particle to collide with a vertical
     * wall, assuming no interening collisions.
     *
     * @return the amount of time for this particle to collide with a vertical
     * wall, assuming no interening collisions; {@code Double.POSITIVE_INFINITY}
     * if the particle will not collide with a vertical wall
     */
    public double timeToHitVerticalWall() {
        if (vx > 0) {
            return (1.0 - rx - radius) / vx;
        } else if (vx < 0) {
            return (radius - rx) / vx;
        } else {
            return INFINITY;
        }
    }

    /**
     * Returns the amount of time for this particle to collide with a horizontal
     * wall, assuming no interening collisions.
     *
     * @return the amount of time for this particle to collide with a horizontal
     * wall, assuming no interening collisions; {@code Double.POSITIVE_INFINITY}
     * if the particle will not collide with a horizontal wall
     */
    public double timeToHitHorizontalWall() {
        if (vy > 0) {
            return (1.0 - ry - radius) / vy;
        } else if (vy < 0) {
            return (radius - ry) / vy;
        } else {
            return INFINITY;
        }
    }

    /**
     * Updates the velocities of this particle and the specified particle
     * according to the laws of elastic collision. Assumes that the particles
     * are colliding at this instant.
     *
     * @param that the other particle
     */
    public void bounceOff(Particle that) {
        double dx = that.rx - this.rx;
        double dy = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        double dvdr = dx * dvx + dy * dvy;             // dv dot dr
        double dist = this.radius + that.radius;   // distance between particle centers at collison

        // magnitude of normal force
        double magnitude = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);

        // normal force, and in x and y directions
        double fx = magnitude * dx / dist;
        double fy = magnitude * dy / dist;

        String saveThisBefore = "this before: " + this + "KE: " + this.kineticEnergy();
        String saveThatBefore = "that before: " + that + "KE: " + that.kineticEnergy();
        double energyBefore = this.kineticEnergy() + that.kineticEnergy();
        double momentumBefore = this.momentum() + that.momentum();
        double[] speeds = calculate1DVelocity(that);

        // update velocities according to normal force
        this.vx += fx / this.mass;
        this.vy += fy / this.mass;
        that.vx -= fx / that.mass;
        that.vy -= fy / that.mass;

        // update collision counts
        this.count++;
        that.count++;

        double totaldiff = Math.abs(this.vx - speeds[0]) + Math.abs(that.vx - speeds[1]);
        double energyAfter = this.kineticEnergy() + that.kineticEnergy();
        double momentumAfter = this.momentum() + that.momentum();
        debug("difference between starting and ending energy: " + Math.abs(energyAfter - energyBefore));
        debug("difference between starting and ending momentum: " + Math.abs(momentumAfter - momentumBefore));
        debug("Total difference in speed from my 2Dcalculation: : " + totaldiff + "");
        debug(saveThisBefore);
        debug("this after: " + this + "KE: " + this.kineticEnergy());
        debug(saveThatBefore);
        debug("that after: " + that + "KE: " + that.kineticEnergy());
        debug("");

        if (totaldiff > 0.00000001) {
            throw new RuntimeException("Total Difference too high: " + totaldiff);
        }
    }

    /**
     * Updates the velocity of this particle upon collision with a vertical wall
     * (by reflecting the velocity in the <em>x</em>-direction). Assumes that
     * the particle is colliding with a vertical wall at this instant.
     */
    public void bounceOffVerticalWall() {
        vx = -vx;
        count++;
    }

    /**
     * Updates the velocity of this particle upon collision with a horizontal
     * wall (by reflecting the velocity in the <em>y</em>-direction). Assumes
     * that the particle is colliding with a horizontal wall at this instant.
     */
    public void bounceOffHorizontalWall() {
        vy = -vy;
        count++;
    }

    /**
     * Returns the kinetic energy of this particle. The kinetic energy is given
     * by the formula 1/2 <em>m</em> <em>v</em><sup>2</sup>, where <em>m</em> is
     * the mass of this particle and <em>v</em> is its velocity.
     *
     * @return the kinetic energy of this particle
     */
    public double kineticEnergy() {
        return 0.5 * mass * (vx * vx + vy * vy);
    }

    public double momentum() {
        return mass * Math.sqrt(vx * vx + vy * vy);
    }

    private double[] calculate1DVelocity(Particle that) {
        // this calculations first
        double part1 = (this.mass - that.mass) / (this.mass + that.mass) * this.vx;
        double part2 = 2 * that.mass / (this.mass + that.mass) * that.vx;
        double[] answer = new double[2];
        answer[0] = part1 + part2;
        part1 = 2 * this.mass / (this.mass + that.mass) * this.vx;
        part2 = (that.mass - this.mass) / (this.mass + that.mass) * that.vx;
        answer[1] = part1 + part2;
        return answer;
    }
    private final static boolean debug = true;

    private void debug(String s) {
        System.out.println(s);
    }

    void setColor(Color c) {
        color = c;
    }

    @Override
    public String toString() {
        return String.format("rx: %5.3f, ry: %5.3f, vx: %5.3f, vy: %5.3f, energy: "
                + "%.2e, momentum: %.2e, radius: %5.3f, mass: %5.3f, color: %s, count: %d",
                rx, ry, vx, vy, kineticEnergy(), momentum(), radius, mass, color, count);

    }
}
