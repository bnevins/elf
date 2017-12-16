/*
 * UsersManager.java
 *
 * Created on September 2, 2007, 7:12 PM
 *
 */

package com.elf.enterprise.auth;

import com.elf.enterprise.db.*;
import com.elf.enterprise.email.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.naming.*;

/**
 *
 * @author bnevins
 */

public class UsersManager
{
    public static synchronized UsersManager getInstance(String newJndi)
    {
        if(instance == null  || !newJndi.equals(instance.jndi))
        {
            try
            {
                instance = new UsersManager(newJndi);
            } catch (NamingException ex)
            {
                ex.printStackTrace();
            } catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
        
        return instance;
    }
    private UsersManager(String newjndi) throws NamingException, SQLException
    {
        jndi = newjndi;
        usersDB = new UsersDB(jndi);
    }

    public synchronized boolean verifyDB() throws NamingException, SQLException
    {
        return usersDB.verifyDB();
    }

    public synchronized void validateUser(String userid, int signupKeyFromUser) 
        throws UsersManagerException
    {
        usersDB.validateUser(userid, signupKeyFromUser);
    }
    public synchronized void createSchema() throws IOException, NamingException, SQLException
    {
            usersDB.createSchema(); 
    }
    public synchronized User[] getUsers(boolean refresh)
    {
        if(refresh || users == null)
            readUsers();
        
        return users;
    }

    public synchronized String[] getAllRoles(boolean refresh)
    {
        // this is inefficient and could be implemented with a DB query instead...
        if(refresh)
            readUsers();

        Set<String> allRoles = new HashSet<String>();
        
        for(User user : users)
        {
            String[] roles = user.getRoles();
            for(String role : roles)
            {
                allRoles.add(role);
            }
        }
        
        return allRoles.toArray(new String[allRoles.size()]);
    }
    
    public synchronized void addUser(String un, String pw, String fn, String ln, 
        String em, String[] roles)
    {
        try
        {
            usersDB.addUser(un, pw, fn, ln, em, roles);
            readUsers();
            
            if(!ok(em))
                return;
            
            int signupKey = usersDB.getSignupId(un);

            if(signupKey < 0)
                return; // should not happen
            
            Emailer emailer = new Emailer();
            emailer.setTo(em);
            emailer.setFrom("registrar@bnevins.com");
            emailer.setSubject("Your new BNevins Account");
            emailer.setBody("Thanks for opening your new account.  Please click " +
                "on the link to finalize registration.\n\n" +
                "http://www.bnevins.com/AuthAdmin/Signup?signupKey=" + signupKey);
            emailer.send();

            emailer.setTo("registrar@bnevins.com");
            emailer.setFrom("registrar@bnevins.com");
            emailer.setSubject("Your new BNevins Account");
            String body = "Thanks for opening your new account.  Please click " +
                "on the link to finalize registration.\n\n" +
                "http://www.bnevins.com/AuthAdmin/Signup?signupKey=" + signupKey +
                "\n\n" +
                "Username: " + un +
                "\nEMail: " + em +
                "\nPassword: " + pw +
                "\nFirst Name: " + fn +
                "\nLast Name: " + ln +
                "\nRoles: ";
            
            for(String role : roles)
            {
                body += role;
                body += ", ";
            }
            emailer.setBody(body);
            emailer.send();
        } 
        catch(Exception ex)
        {
            ex.printStackTrace();
        } 
    }

    private synchronized void readUsers()
    {
        users = new User[0];
        try
        {
            users = usersDB.read();
        } 
        catch(Exception ex)
        {
            ex.printStackTrace();
        } 
    }
    
    public synchronized void deleteUser(String un)
    {
        try
        {
            usersDB.deleteUser(un);
        } 
        catch(Exception ex)
        {
            ex.printStackTrace();
        } 
    }

    public synchronized void updateUser(User user)
    {
        try
        {
            usersDB.updateUser(user);
        } 
        catch(Exception ex)
        {
            ex.printStackTrace();
        } 
    }
    
    public synchronized String getFirstName(String id)
    {
        for(User user : users)
        {
            if(user.getUserName().equals(id))
                return user.getFirstName();
        }
        return "";
    }

    private boolean ok(String s)
    {
        return s != null && s.length() > 0;
    }

    private static UsersManager instance;

    private User[] users;
    private UsersDB usersDB;
    private String jndi;
}
