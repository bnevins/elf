/*
 * WeightItem.java
 *
 * Created on October 27, 2007, 12:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.enterprise.weight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class WeightItem
{
    public WeightItem(ResultSet rs) throws SQLException
    {
        setId(rs.getInt("WEIGHT_ID"));
        setPerson(rs.getString("PERSON"));
        setWeight(rs.getDouble("WEIGHT"));
        setDate(rs.getTimestamp("DATE"));
    }

    private Date    date;
    private int     id;
    private double     weight;
    private String person;

    public Date getDate()
    {
        return date;
    }

    public String getDateString()
    {
        // e.g. Oct 28, 2007
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
    }

    private void setDate(Date date)
    {
        this.date = date;
    }

    public int getId()
    {
        return id;
    }

    public double getWeight()
    {
        return weight;
    }

    private void setWeight(double weight)
    {
        this.weight = weight;
    }

    private void setId(int id)
    {
        this.id = id;
    }

    private void setPerson(String person)
    {
        this.person = person;
    }

    public String getPerson()
    {
        return person;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(",");
        sb.append(person);
        sb.append(",");
        sb.append(weight);
        sb.append(",");
        sb.append(date);

        return sb.toString();
    }

    public String flatten()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(person);
        sb.append(",");
        sb.append(weight);
        sb.append(",");
        sb.append(date.getTime());

        return sb.toString();
    }
}
