/*
 * Globals.java
 *
 * Created on September 3, 2007, 11:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.elf.webapps.todo.model;

/**
 *
 * @author bnevins
 */
public class Globals {
    private Globals() {
    }
    public final static boolean debug = true;
    public final static String jndi = "jdbc/bnevins";
    public final static String TODO_TABLE_NAME = "todo_items";
    //public final static String  MAIL_SMTP_HOST       = "smtp.dslextreme.com";
    public final static String MAIL_FROM = "ToDoList@bnevins.com";
}
