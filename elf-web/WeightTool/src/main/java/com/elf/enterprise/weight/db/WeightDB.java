/*
 * WeightDB.java
 *
 * Created on September 28, 2007, 9:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.elf.enterprise.weight.db;

import com.elf.enterprise.weight.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.*;
import java.io.*;
import com.elf.enterprise.db.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author bnevins
 */
public class WeightDB {

    public WeightDB() {
    }

    public List<WeightItem> getItems() throws NamingException, SQLException {
        read();
        return items;
    }

    public List<WeightItem> getItems(String who) throws NamingException, SQLException {
        String whereClause = " where PERSON = " + singleQuote(who) + " ";
        read(whereClause);
        return items;
    }

    public boolean verifySchema() {
        try {
            Connection conn = getConnection();
            Statement query = conn.createStatement();
            String q = "select TABLENAME from sys.systables where TABLENAME = '"
                    + Globals.WEIGHT_TABLE_NAME
                    + "'";
            if (query.execute(q))
                return true;
            else
                return false;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public boolean verifyDBAlive() {
        Connection conn = null;
        Statement query = null;
        try {
            conn = getConnection();
            query = conn.createStatement();
        }
        catch (Exception ex) {
            return false;
        } finally {
            try {
                query.close();
            }
            catch (Exception ex) {
            }
            try {
                conn.close();
            }
            catch (Exception ex) {
            }
        }
        return true;
    }

    public boolean backup(File f) {
        try {
            List<WeightItem> items = getItems();
            System.out.println("items: " + items);
            PrintStream out = new PrintStream(f);
            out.println("####### WeightTool Backup performed: " + new Date());

            for (WeightItem item : items)
                out.println(item.flatten());

            try {
                //out.flush();
                out.close();
            }
            catch (Exception ex) {
            }

            return true;
        }
        catch (Exception e) {
            Logger.getLogger(WeightDB.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public int restore(File f) throws FileNotFoundException, IOException, NamingException, SQLException {
        BufferedReader br = null;
        int numRecords = 0;
        try {
            br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null) {
                numRecords += restoreLine(line);
            }
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    // nothing to do}
                }
            }
        }
        return numRecords;
    }

    private int restoreLine(String line) throws NamingException, SQLException {
        if (line.startsWith("#")) {
            return 0;
        }
        if (line.indexOf(',') >= 0)
            return restoreLineTimeMsec(line);

        return restoreLineTimeVerbose(line);
    }

    private int restoreLineTimeVerbose(String line) throws NamingException, SQLException {
        // output from select * from weight_items:
        //711        |186.5                 |2011-03-27 13:19:03.978   |bnevins
        // split on spaces gets this: "711", "|186.5", "|2011-03-27", "13:19:03.978", "|bnevins"
        String[] vals = line.split(" ");
        vals = stripEmpties(vals);
        if (vals == null || vals.length != 5) {
            return 0;
        }

        String person = vals[4].substring(1); // "|name" --> "name"
        double wt = Double.parseDouble(vals[1].substring(1));
        String dayString = vals[2].substring(1);

        Date day = java.sql.Date.valueOf(dayString);
        long timeMS = day.getTime() + TWO_HOURS;
        addItem(wt, person, timeMS);
        return 1;
    }

    private int restoreLineTimeMsec(String line) throws NamingException, SQLException {
        String[] vals = line.split(",");

        if (vals == null || vals.length != 3) {
            return 0;
        }
        String person = vals[0];
        double wt = Double.parseDouble(vals[1]);
        long timeMS = Long.parseLong(vals[2]);
        addItem(wt, person, timeMS);
        return 1;
    }

    public void createSchema(boolean force) throws NamingException, SQLException {
        Connection conn = null;
        Statement query = null;

        try {
            conn = getConnection();
            query = conn.createStatement();

            // 
            if (force)
                try {
                    query.execute("DROP TABLE " + Globals.WEIGHT_TABLE_NAME);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            query.execute(CREATE_ALL_QUERY);
        } finally {
            try {
                query.close();
            }
            catch (Exception ex) {
            }
            try {
                conn.close();
            }
            catch (Exception ex) {
            }
        }
    }

    public void addItem(double weight, String person) throws NamingException, SQLException {
        delete(alreadyRecorded(weight, person));

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            String query =
                    "INSERT INTO "
                    + Globals.WEIGHT_TABLE_NAME
                    + " (WEIGHT, PERSON) "
                    + " values (?,?)";

            ps = conn.prepareStatement(query);

            ps.setDouble(1, weight);
            ps.setString(2, person);
            ps.executeUpdate();
        } finally {
            try {
                ps.close();
            }
            catch (Exception ex) {
            }
            try {
                conn.close();
            }
            catch (Exception ex) {
            }
        }
    }

    public void addItem(double weight, String person, long timeMS) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            String query =
                    "INSERT INTO "
                    + Globals.WEIGHT_TABLE_NAME
                    + " (WEIGHT, PERSON, DATE) "
                    + " values (?,?,?)";

            ps = conn.prepareStatement(query);

            ps.setDouble(1, weight);
            ps.setString(2, person);
            ps.setDate(3, new java.sql.Date(timeMS));
            ps.executeUpdate();
        } finally {
            try {
                ps.close();
            }
            catch (Exception ex) {
            }
            try {
                conn.close();
            }
            catch (Exception ex) {
            }
        }
    }

    public void delete(int deleteID) throws SQLException, NamingException {
        if (deleteID < 0)
            return; // normal.  Makes calling code cleaner

        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM WEIGHT_ITEMS WHERE WEIGHT_ID = ?");
        ps.setInt(1, deleteID);
        int nrows = ps.executeUpdate();
    }

    private void read() throws NamingException, SQLException {
        read(null);
    }

    private void read(String whereClause) throws NamingException, SQLException {
        String queryString = QUERY_STRING;

        if (ok(whereClause))
            queryString += " " + whereClause;

        queryString += " order by date desc ";
        Connection conn = null;
        Statement query = null;
        items.clear();

        try {
            conn = getConnection();
            query = conn.createStatement();
            ResultSet rs = getResults(query, queryString);

            while (rs != null && rs.next())
                items.add(new WeightItem(rs));
        } finally {
            try {
                query.close();
            }
            catch (Exception ex) {
            }
            try {
                conn.close();
            }
            catch (Exception ex) {
            }
        }
    }

    private ResultSet getResults(Statement statement, String query) throws SQLException {
        // bnevins, January 2011.  If this is the very first time WeightTool
        // is called then there is no table in the DB yet.  In that case --
        // try to create one before returning.
        // If there are no rows we will NOT get an Exception.  If there is no
        // table  then we will definitely get a SQLException.
        try {
            return statement.executeQuery(query);
        }
        catch (SQLException sex) {
            try {
                createSchema(false);
                // No exception -- problem is probably solved now!
            }
            catch (Exception dontCare) {
                // throw the original exception -- not the one we got trying to
                //fix the problem...
                throw sex;
            }
        }
        return null;
    }

    /* super ultra complicated!  Should do this with a SQL statement instead...
     * TODO
     */
    public int alreadyRecorded(double weight, String person) {
        Date nowDate = new Date();
        Calendar nowCal = Calendar.getInstance();
        long nowMsec = nowDate.getTime();
        nowCal.setTime(nowDate);

        int previousID = -1;
        Date previousDate = null;

        System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(nowDate) + " person= " + person);

        try {
            read();
        }
        catch (Exception e) {
        }

        // go through all the items and find the latest one
        for (WeightItem record : items) {
            if (previousDate == null) {
                // first one...
                previousDate = record.getDate();
                previousID = record.getId();
            } else if (record.getDate().getTime() > previousDate.getTime()) {
                previousID = record.getId();
                previousDate = record.getDate();
            }
        }

        if (previousDate == null) // this is the very first entry for this person...
            return -1;

        // A quick check.  If it has been more than 24 hours -- get outta here!
        if (nowDate.getTime() - previousDate.getTime() > ONE_DAY_IN_MSEC)
            return -1;

        // It's been less than 24 hours.  Let's say it's been 5 minutes -- but
        // previousDate was 11:59PM and now is 12:04 AM
        // then we should NOT delete previousDate -- it is officially a new day.
        Calendar previousCal = Calendar.getInstance();
        previousCal.setTime(previousDate);
        int previousDayOfYear = previousCal.get(Calendar.DAY_OF_YEAR);
        int previousYear = previousCal.get(Calendar.YEAR);

        int nowDayOfYear = nowCal.get(Calendar.DAY_OF_YEAR);
        int nowYear = nowCal.get(Calendar.YEAR);

        // OMG !  what a nightmare!!!
        if ((nowYear == previousYear) && (nowDayOfYear == previousDayOfYear)) {
            System.out.println("Weight already recorded for today.  Overwriting # " + previousID);
            return previousID;
        }

        return -1;
    }

    private String singleQuote(String s) {
        return "'" + s + "'";
    }

    private boolean ok(String s) {
        return s != null && s.length() > 0;
    }

    static String[] stripEmpties(String[] in) {
        List<String> list = new ArrayList<String>();

        for(String s : in) {
            if(s != null && s.length() > 0)
                list.add(s);
        }
        return list.toArray(new String[list.size()]);
    }

    private List<WeightItem> items = new ArrayList<WeightItem>();
    private String QUERY_STRING = "SELECT * FROM " + Globals.WEIGHT_TABLE_NAME;
    private final static String CREATE_ALL_QUERY =
            "CREATE TABLE "
            + Globals.WEIGHT_TABLE_NAME
            + "(WEIGHT_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT WEIGHT_ITEM_PK PRIMARY KEY, "
            + " PERSON VARCHAR(32) NOT NULL, "
            + " WEIGHT DOUBLE NOT NULL, "
            + " DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ")";
    private static final long ONE_DAY_IN_MSEC = 24 * 60 * 60 * 1000;

    private Connection getConnection() throws NamingException, SQLException {
        return DBHelper.getConnection(Globals.JDBC_RESOURCE_NAME);
    }
    private static final long TWO_HOURS = 1000 * 60 * 60 * 2;
}
