/*
 * Globals.java
 *
 * Created on October 6, 2007, 3:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package admin;

/**
 *
 * @author bnevins
 */
public class Globals
{
    private Globals()
    {
    }
    // sometimes we need this -- e.g. signup where we are working with one particular
    // jndi name all the time.  When Admin GUI is going -- they can change the jndi to
    // play with other DBs.
    public final static String  jndi = "jdbc/bnevins";
}
