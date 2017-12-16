/*
 * ToDoDBManager.java
 *
 * Created on April 3, 2011
 *
 */
package com.elf.webapps.todo.db;

import com.elf.enterprise.db.DBHelper;
import com.elf.webapps.todo.model.Globals;
import com.elf.webapps.todo.model.ToDoItem;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author bnevins
 */
public class ToDoDBManager {
    public ToDoDBManager() {
        this.jndi = Globals.jndi;
    }

    /**
     * Rarely used methods for writing to a file and reading from a file
     */
    public void writeToDisk(File f) throws NamingException, SQLException, IOException {
        List<ToDoItem> theitems = getItems();
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(f));

        oos.writeObject(theitems);
        oos.close();
    }

    public List<ToDoItem> readFromDisk(File f) throws NamingException, SQLException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(f));

        List<ToDoItem> theitems = (List<ToDoItem>) ois.readObject();
        ois.close();
        return theitems;
    }

    public List<ToDoItem> getItems(HttpServletRequest request) throws NamingException, SQLException {
        String user = request.getUserPrincipal().getName();
        read(user);
        return items;
    }
    public List<ToDoItem> getItems(String where) throws NamingException, SQLException {
        read(where);
        return items;
    }

    public List<ToDoItem> getItems() throws NamingException, SQLException {
        read(null);
        return items;
    }

    public void read(String whom) throws NamingException, SQLException {
        Connection conn = null;
        Statement query = null;
        items.clear();

        String queryString = "SELECT * FROM " + Globals.TODO_TABLE_NAME;

        // fixme todo
        if (ok(whom)) {
            queryString += " where assignee = '" + whom + "' OR assigner = '" + whom + "'";
        }
        try {
            conn = DBHelper.getConnection(jndi);
            query = conn.createStatement();
            ResultSet rs = null;
            rs = queryAndCreateTableIfNeeded(query, queryString);

            while (rs.next()) {
                items.add(new ToDoItem(rs));
            }
        }
        finally {
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

    public boolean verifySchema() {
        try {
            Connection conn = DBHelper.getConnection(jndi);
            Statement query = conn.createStatement();

            // TODO
            String q = "select TABLENAME from sys.systables where TABLENAME = '"
                    + Globals.TODO_TABLE_NAME
                    + "'";
            if (query.execute(q)) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception ex) {
            return false;
        }
    }

    public boolean verifyDBAlive() {
        Connection conn = null;
        Statement query = null;
        try {
            conn = DBHelper.getConnection(jndi);
            query = conn.createStatement();
        }
        catch (Exception ex) {
            return false;
        }
        finally {
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

    public void createSchema(boolean force) throws NamingException, SQLException {
        Connection conn = null;
        Statement query = null;

        try {
            conn = DBHelper.getConnection(jndi);
            query = conn.createStatement();

            // 
            if (force) {
                try {
                    query.execute("DROP TABLE " + Globals.TODO_TABLE_NAME);
                }
                catch (Exception e) {
                    // ignore
                }
            }
            
            String[] sqlcmds = ReadResourceAsSQL("/sql/create_todo.sql");
            
            for (String sqlcmd : sqlcmds) {
                query.execute(sqlcmd);
            }

            /*
             * query.execute(CREATE_ALL_QUERY); query.execute(FK1);
             * query.execute(FK2);
             */
        }
        finally {
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

    public void closeItem(int id) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBHelper.getConnection(jndi);
            ps = conn.prepareStatement(
                    "UPDATE "
                    + Globals.TODO_TABLE_NAME
                    + " SET CLOSED_DATE=?"
                    + " WHERE ITEM_ID=?");
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, id);
            ps.executeUpdate();
        }
        finally {
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

    public void reopenItem(int id) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBHelper.getConnection(jndi);
            ps = conn.prepareStatement(
                    "UPDATE "
                    + Globals.TODO_TABLE_NAME
                    + " SET CLOSED_DATE=NULL"
                    + " WHERE ITEM_ID=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        finally {
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

    public void addItem(ToDoItem item) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            //insert into TODO_ITEMS (DESCRIPTION, ASSIGNEE, ASSIGNER) values ('xxxx', 'byron', 'fred');

            conn = DBHelper.getConnection(jndi);
            String query =
                    "INSERT INTO "
                    + Globals.TODO_TABLE_NAME
                    + " (DESCRIPTION, ASSIGNEE, ASSIGNER) "
                    + " values (?,?,?)";

            ps = conn.prepareStatement(query);

            ps.setString(1, item.getDescription());
            ps.setString(2, item.getAssignedTo());
            ps.setString(3, item.getAssignedBy());
            ps.executeUpdate();

            /*
             * conn = DBHelper.getConnection(jndi); ps = conn.prepareStatement(
             * "INSERT INTO " + Globals.TODO_TABLE_NAME + " (DESCRIPTION,
             * ASSIGNEE, ASSIGNER) " + " values (" +
             * singleQuote(item.getDescription()) + ", " +
             * singleQuote(item.getAssignedTo()) + ", " +
             * singleQuote(item.getAssignedBy()) + ")" ); ps.setInt(1,
             * item.getId()); ps.executeUpdate();
             */
        }
        finally {
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

    private String singleQuote(String s) {
        return "'" + s + "'";
    }

    private boolean ok(String s) {
        return s != null && s.length() > 0;
    }

    public void delete(int deleteID) throws SQLException, NamingException {
        Connection conn = DBHelper.getConnection(jndi);
        PreparedStatement ps = conn.prepareStatement("DELETE FROM TODO_ITEMS WHERE ITEM_ID = ?");
        ps.setInt(1, deleteID);
        int nrows = ps.executeUpdate();
    }

    public void toggleOpenAndClosed(int showHideID) throws NamingException, SQLException {
        Connection conn = DBHelper.getConnection(jndi);
        PreparedStatement ps1 = conn.prepareStatement("SELECT CLOSED_DATE FROM "
                + Globals.TODO_TABLE_NAME + " WHERE ITEM_ID = ?");
        ps1.setInt(1, showHideID);
        ResultSet rs = ps1.executeQuery();
        rs.next();
        Timestamp ts = rs.getTimestamp(1);

        // if ts != NULL then the item is closed so reopen it

        if (ts == null) // it is currently open
        {
            closeItem(showHideID);
        }
        else {
            reopenItem(showHideID);
        }
    }

    private ResultSet queryAndCreateTableIfNeeded(Statement query, String queryString)
            throws NamingException, SQLException {
        try {
            return query.executeQuery(queryString);
        }
        catch (SQLException se) {
            // handle the case where the table hasn't been created yet.
            // Semi-hack.  Be careful if you want to clean this up...
            createSchema(true);
            return query.executeQuery(queryString);
        }
    }

    private String[] ReadResourceAsSQL(String where) {
        final String[] empty = new String[0];

        try {
            InputStream is = getClass().getResourceAsStream(where);

            if (is == null) {
                return empty;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            String all = sb.toString();

            if (all.isEmpty()) {
                return empty;
            }

            all = all.trim();
            return all.split(";");
        }
        catch (IOException ex) {
            return empty;
        }
    }
    private String jndi;
    private List<ToDoItem> items = new ArrayList<ToDoItem>();
    private final static String CREATE_ALL_QUERYXX =
            "CREATE TABLE "
            + Globals.TODO_TABLE_NAME
            + "(ITEM_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT TODO_ITEM_PK PRIMARY KEY, "
            + " DESCRIPTION VARCHAR(64) NOT NULL, "
            + " ASSIGNEE VARCHAR(32) NOT NULL, "
            + " ASSIGNER VARCHAR(32), "
            + " OPENED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + " CLOSED_DATE TIMESTAMP "
            + ")";
    //+ 
    //+ " ALTER TABLE TODO_ITEMS ADD CONSTRAINT ASSIGNER_FK FOREIGN KEY(ASSIGNEE) REFERENCES USERTABLE(USERID) on delete cascade on update restrict;";
    private final static String CREATE_ALL_QUERY =
            "CREATE TABLE TODO_ITEMS (        ITEM_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT TODO_ITEM_PK PRIMARY KEY,        DESCRIPTION VARCHAR(64) NOT NULL,        ASSIGNEE VARCHAR(32) NOT NULL,        ASSIGNER VARCHAR(32),        OPENED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,        CLOSED_DATE TIMESTAMP)";
    private final static String FK1 = " ALTER TABLE TODO_ITEMS ADD CONSTRAINT ASSIGNEE_FK FOREIGN KEY(ASSIGNEE) REFERENCES USERTABLE(USERID) on delete cascade on update restrict";
    private final static String FK2 = " ALTER TABLE TODO_ITEMS ADD CONSTRAINT ASSIGNEE_FK FOREIGN KEY(ASSIGNEE) REFERENCES USERTABLE(USERID) on delete cascade on update restrict";
}
