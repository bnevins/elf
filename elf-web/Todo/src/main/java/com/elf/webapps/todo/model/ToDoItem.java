/*
 * ToDoItem.java
 *
 * Created on August 25, 2007, 10:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.elf.webapps.todo.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

/*
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
 */
/**
 *
 * @author bnevins
 */
public final class ToDoItem implements Serializable {

    public String getDescription() {
        return description;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public Date getDateOpened() {
        return dateOpened;
    }

    public String getDateOpenedString() {
        return getDateString(dateOpened);
    }

    public Date getDateClosed() {
        return dateClosed;
    }

    public String getDateClosedString() {
        if (isClosed())
            return getDateString(dateClosed);
        else
            return "OPEN";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setDateOpened(Date dateOpened) {
        this.dateOpened = dateOpened;
    }

    public void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isClosed() {
        return getDateClosed() != null;
    }

    public String getButtonText() {
        if (isClosed())
            return REOPEN;
        else
            return COMPLETED;
    }

    ///////////////////////////////////////////////////////////////////////////
    public ToDoItem(String desc, String assigner, String assignee) throws SQLException {
        setId(-1);
        setDescription(desc);
        setAssignedTo(assignee);
        setAssignedBy(assigner);
    }

    ///////////////////////////////////////////////////////////////////////////
    public ToDoItem(ResultSet rs) throws SQLException {
        // it's simple.  For now...
        setId(rs.getInt(1));
        setDescription(rs.getString(2));
        setAssignedTo(rs.getString(3));
        setAssignedBy(rs.getString(4));
        setDateOpened(rs.getTimestamp(5));
        setDateClosed(rs.getTimestamp(6));
    }

    @Override
    public int hashCode() {
        return getId();
    }

    private String getDateString(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date);
    }
    private String description;
    private String assignedBy;
    private String assignedTo;
    private Date dateOpened;
    private Date dateClosed;
    private int id;
    private final static String SEP = ",,,";
    public final static String COMPLETED = "Close";
    public final static String REOPEN = "Reopen";
}
