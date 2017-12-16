/*
 * DBHelper.java
 *
 * Created on September 23, 2007, 11:19 AM
 *
 */
package com.elf.enterprise.db;

import java.io.File;
import java.security.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;
import javax.sql.*;

/**
 *
 * @author bnevins
 */
public class DBHelper {
    private DBHelper() {
    }

    /*
     * Assumes we are running inside GlassFish
     */
    public static Connection getConnection(String jndi) 
            throws NamingException, SQLException {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(jndi);
            return ds.getConnection();
    }

    /*
     * assume we are running outside GF
     */
    public static Connection getConnection(File dbDir) {
        try {
            String driver = "org.apache.derby.jdbc.Driver30";
            String connectionURL = "jdbc:derby://localhost:1527/"
                    + dbDir.getAbsolutePath().replace("\\", "/")
                    + ";create=true";
            Class.forName(driver);
            System.out.println(driver + " loaded. ");
            return DriverManager.getConnection(connectionURL);
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String hashPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException ex) {
            // can't happen!
            return password;
        }

        byte[] bytes = password.getBytes();

        synchronized (md) {
            md.reset();
            bytes = md.digest(bytes);
        }

        return hexEncode(bytes);
    }

    private static String hexEncode(byte[] bytes) {
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            int low = (int) (bytes[i] & 0x0f);
            int high = (int) ((bytes[i] & 0xf0) >> 4);
            sb.append(HEXADECIMAL[high]);
            sb.append(HEXADECIMAL[low]);
        }
        return sb.toString();
    }
    private static final char[] HEXADECIMAL = {'0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
}
