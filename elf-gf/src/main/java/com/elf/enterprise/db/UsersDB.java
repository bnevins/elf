/*
 * UsersDB.java
 *
 * Created on September 23, 2007, 11:18 AM
 *
 */
package com.elf.enterprise.db;

import com.elf.enterprise.auth.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import javax.naming.*;

/**
 *
 * @author bnevins Schema: Table: usertable: userid, password Table: grouptable:
 * userid, groupid Table: userinfo: userid, fname, lname, email
 */
public class UsersDB {
    public UsersDB(String jndi) throws NamingException, SQLException {
        this.jndi = jndi;
        this.dbPath = null;
    }

    public UsersDB(File dbPath) throws NamingException, SQLException {
        this.dbPath = dbPath;
        this.jndi = null;

    }

    public synchronized User[] read() throws NamingException, SQLException {
        Connection connection = null;
        Statement statement1 = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            statement1 = connection.createStatement();
            String query = getUserInfoQuery();
            ResultSet rs1 = statement1.executeQuery(query);
            List<User> list = new ArrayList<User>();

            while (rs1.next()) {
                String un = rs1.getString("userid");
                String hpw = rs1.getString("password");
                String fn = rs1.getString("firstname");
                String ln = rs1.getString("lastname");
                String em = rs1.getString("email");

                // now get the roles for this user...

                ps = connection.prepareStatement(GET_USER_ROLE_QUERY);
                ps.setString(1, un);
                ResultSet rs2 = ps.executeQuery();
                List<String> rolesList = new ArrayList<String>();
                while (rs2.next()) {
                    rolesList.add(rs2.getString(1));
                }
                String[] roles = rolesList.toArray(new String[rolesList.size()]);
                rs2.close();
                ps.close();

                // finally -- get the signupID.  The ID is GUARANTEED to exist and be
                // valid.  If the userid isn't in the table -- then he has been verified already.


                int suid = getSignupId(un, connection);
                list.add(new User(un, hpw, fn, ln, em, roles, suid));
            }

            return list.toArray(new User[list.size()]);
        }
        finally {
            try {
                connection.close();
            }
            catch (Exception e) {
            }
            try {
                statement1.close();
            }
            catch (Exception e) {
            }
            try {
                ps.close();
            }
            catch (Exception e) {
            }
        }
    }

    public synchronized void addUser(String un, String pw, String fn,
            String ln, String em, String[] roles)
            throws NamingException, SQLException {
        PreparedStatement ps = null;
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            pw = DBHelper.hashPassword(pw);
            ps = connection.prepareStatement(ADD_USER_QUERY_CORE);
            ps.setString(1, un);
            ps.setString(2, pw);
            ps.executeUpdate();
            ps.close();

            ps = connection.prepareStatement(ADD_USER_QUERY_INFO);
            ps.setString(1, un);
            ps.setString(2, fn);
            ps.setString(3, ln);
            ps.setString(4, em);
            ps.executeUpdate();
            ps.close();

            ps = connection.prepareStatement(ADD_USER_ROLE_QUERY);

            for (String role : roles) {
                ps.setString(1, un);
                ps.setString(2, role);
                ps.executeUpdate();
            }
            ps.close();


            ps = connection.prepareStatement(ADD_USER_SIGNUP_QUERY);
            ps.setString(1, un);
            ps.executeUpdate();
            ps.close();

            connection.commit();
        }
        finally {
            connection.setAutoCommit(true);
            try {
                ps.close();
            }
            catch (Exception e) {
            }
            try {
                connection.close();
            }
            catch (Exception e) {
            }
        }
    }

    /**
     * password is handled specially because of the hashing
     */
    public synchronized void updateUser(User user, String clearPassword) throws NamingException, SQLException {
        user.setHashedPassword(DBHelper.hashPassword(clearPassword));
        updateUser(user);
    }

    public synchronized void updateUser(User user) throws NamingException, SQLException {
        PreparedStatement ps = null;
        Connection connection = null;

        try {
            connection = getConnection();
            String un = user.getUserName();
            ps = connection.prepareStatement(UPDATE_PASSWORD);
            ps.setString(1, user.getHashedPassword());
            ps.setString(2, un);
            ps.executeUpdate();

            ps = connection.prepareStatement(UPDATE_USER_INFO);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, un);
            ps.executeUpdate();
            ps.close();

            // crude way to do it (but it works!) -- wipe out all groups and then add them back in
            ps = connection.prepareStatement(DELETE_USER_ROLE_QUERY);
            ps.setString(1, un);
            ps.executeUpdate();
            ps.close();

            ps = connection.prepareStatement(ADD_USER_ROLE_QUERY);
            String[] roles = user.getRoles();

            for (String role : roles) {
                ps.setString(1, un);
                ps.setString(2, role);
                ps.executeUpdate();
            }

            connection.commit();
            ps.close();
            connection.close();
        }
        finally {
            try {
                ps.close();
            }
            catch (Exception e) {
            }
            try {
                connection.close();
            }
            catch (Exception e) {
            }
        }
    }

    public synchronized void deleteUser(String un)
            throws NamingException, SQLException {
        PreparedStatement ps = null;
        Connection connection = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(DELETE_USER_QUERY);
            ps.setString(1, un);
            ps.executeUpdate();
            ps = connection.prepareStatement(DELETE_USER_ROLE_QUERY);
            ps.setString(1, un);
            ps.executeUpdate();
            ps.close();
            connection.close();
        }
        finally {
            try {
                ps.close();
            }
            catch (Exception e) {
            }
            try {
                connection.close();
            }
            catch (Exception e) {
            }
        }
    }

    public boolean verifyDB() throws NamingException, SQLException {
        Statement statement = null;
        Connection connection = null;

        try {
            // first test -- if the DB doesn't exist or the jndi name isn't right
            // etc., etc., etc., then an Exception will get thrown

            connection = getConnection();
            statement = connection.createStatement();


            // next test -- are the three tables there?
            ResultSet rs = statement.executeQuery(ALL_THREE_TABLES);

            int numTables = 0;

            while (rs.next()) {
                ++numTables;
            }

            if (numTables < 3) {
                throw new SQLException("The Auth tables are not in the DB.  This is normal if it is a brand-new database.");
            }

            // Final test -- is there at least one Administrator registered in
            // the DB.  If not he can't get past the security required to manage 
            // the DB -- so we demand it.

            rs.close();
            rs = statement.executeQuery(HAS_ADMINISTRATOR);

            if (!rs.next()) {
                throw new SQLException("The Auth tables exist -- but there is no Administrator.");
            }
            else {
                return true;
            }

        }
        finally {
            try {
                connection.close();
            }
            catch (Exception e) {
            }
            try {
                statement.close();
            }
            catch (Exception e) {
            }
        }
    }

    public synchronized void createSchema() throws IOException, NamingException, SQLException {
        Statement statement = null;
        Connection connection = null;

        try {
            connection = getConnection();
            String[] schema = readCreateSchemaScript();
            statement = connection.createStatement();

            // drop the tables.  We don't care if we get an Exception because of
            // the table not existing already.
            // TODO roll into a loop

            try {
                statement.execute("drop table " + USERINFOTABLE);
            }
            catch (Exception e) {
                System.out.println("Error dropping table: " + e);
            }


            try {
                statement.execute("drop table " + ROLETABLE);
            }
            catch (Exception e) {
                System.out.println("Error dropping table: " + e);
            }

            try {
                statement.execute("drop table " + SIGNUPTABLE);
            }
            catch (Exception e) {
                System.out.println("Error dropping table: " + e);
            }

            // this must be the last thing dropped
            try {
                statement.execute("drop table " + USERTABLE);
            }
            catch (Exception e) {
                System.out.println("Error dropping table: " + e);
            }

            for (String sqlCommand : schema) {
                statement.execute(sqlCommand);
            }

            connection.close();
        }
        finally {
            try {
                connection.close();
            }
            catch (Exception e) {
            }
            try {
                statement.close();
            }
            catch (Exception e) {
            }
        }
    }

    public synchronized boolean hasSignupId(String userid) throws NamingException, SQLException {
        return getSignupId(userid) >= 0;
    }

    public synchronized int getSignupId(String userid) throws NamingException, SQLException {
        boolean temporary_temp_fixme_todo = false;

        if (temporary_temp_fixme_todo) {
            return 2;
        }
        else {
            Connection connection = getConnection();
            int signupKeyFromDB = getSignupId(userid, connection);
            connection.close();
            return signupKeyFromDB;
        }
    }

    public synchronized void validateUser(String userid, int signupKeyFromUser)
            throws UsersManagerException {
        PreparedStatement ps = null;
        Connection connection = null;

        try {
            connection = getConnection();
            int signupKeyFromDB = getSignupId(userid, connection);

            if (signupKeyFromDB < 0) {
                return;
            }

            if (signupKeyFromDB != signupKeyFromUser) {
                throw new UsersManagerException("Key does not match");
            }

            // they match!  Now we simply delete the row and they are valid!

            ps = connection.prepareStatement(DELETE_SIGNUPKEY);
            ps.setString(1, userid);
            ps.executeUpdate();
            ps.close();
            connection.close();
        }
        catch (UsersManagerException e) {
            throw e;
        }
        catch (Exception e) {
            throw new UsersManagerException("Error in validateUser", e);
        }
        finally {
            try {
                ps.close();
            }
            catch (Exception e) {
            }
            try {
                connection.close();
            }
            catch (Exception e) {
            }
        }
    }

    /*
     * private void addGroupToList(List<User> list, String un, String gn) {
     * for(User user : list) { if(user.getUserName().equals(un)) {
     * user.getRoles().add(gn); return; } } }
     */
    private synchronized int getSignupId(String userid, Connection connection) throws NamingException, SQLException {
        if (false) {
            return 2;
        }
        else {
            PreparedStatement ps = connection.prepareStatement(GET_SIGNUPKEY);
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                // the user has already been authenticated.
                // he has no signupKey which means he has already gone
                // through this step before.  return - no harm, no foul.
                return -1;
            }

            int signupKeyFromDB = rs.getInt(1);
            rs.close();
            ps.close();
            return signupKeyFromDB;
        }
    }

    private String getUserInfoQuery() {
        return "select USERTABLE.USERID, password, firstname, lastname, email"
                + " from usertable, userinfotable"
                + " where usertable.userid = userinfotable.userid";

        /*
         * return "SELECT " + USERTABLE + ".userid, " + "password, " +
         * "firstname, " + "lastname, " + "email," + "signupkey" + " FROM " +
         * USERTABLE + ", " + USERINFOTABLE + " where " + USERTABLE + ".userid =
         * " + USERINFOTABLE + ".userid";
         */
    }

    private String[] readCreateSchemaScript() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("createschema.sql")));
        char[] buf = new char[5000];
        String schema = "";
        int numRead = 0;

        while ((numRead = reader.read(buf, 0, 5000)) > 0) {
            schema += new String(buf, 0, numRead);
        }

        System.out.println("HERE IS THE SCHEMA!!!" + schema);

        //String[] statements = schema.split(";")
        return schema.split(";");
    }

    private Connection getConnection() throws NamingException, SQLException {
        if (jndi != null) {
            return DBHelper.getConnection(jndi);
        }
        else {
            return getLocalConnection();
        }
    }

    private synchronized Connection getLocalConnection() throws NamingException, SQLException {
        return DBHelper.getConnection(dbPath);
        /**
         *
         * if (localConnection == null) { localConnection =
         * DBHelper.getConnection(dbPath); }
         *
         * return localConnection;
         */
    }
//  The table and column names are ripe for making user-settable.
    private final File dbPath;
    private final String jndi;
    private volatile Connection localConnection;
    private static final String USERTABLE = "USERTABLE";
    private static final String USERINFOTABLE = "USERINFOTABLE";
    private static final String ROLETABLE = "GROUPTABLE";
    //private static final String SELECT_ROLE = "SELECT * FROM " + ROLETABLE;
    private static final String GET_USER_ROLE_QUERY =
            "select groupid from grouptable where userid=?";
    private static final String ADD_USER_QUERY_CORE = "insert into " + USERTABLE
            + "(USERID,PASSWORD) values (?, ?)";
    private static final String ADD_USER_QUERY_INFO = "insert into " + USERINFOTABLE
            + "(USERID,FIRSTNAME,LASTNAME,EMAIL) values (?, ?, ?, ?)";
    private static final String ADD_USER_ROLE_QUERY = "insert into GROUPTABLE(USERID,GROUPID) "
            + "values (?, ?)";
    private static final String DELETE_USER_QUERY = "delete from " + USERTABLE
            + " where  USERID = ? ";
    private static final String DELETE_USER_ROLE_QUERY = "delete from " + ROLETABLE
            + " where  USERID = ? ";
    private static final String UPDATE_PASSWORD = "update " + USERTABLE + " set password=? where USERID=?";
    private static final String UPDATE_USER_INFO = "update " + USERINFOTABLE
            + " set FIRSTNAME=?, LASTNAME=?, EMAIL=? where USERID=?";
    private static final String CLOSE_ITEM = "update " + USERINFOTABLE
            + " set FIRSTNAME=?, LASTNAME=?, EMAIL=? where USERID=?";
    private static final String ALL_THREE_TABLES =
            " select TABLENAME from sys.systables where "
            + " TABLENAME = '" + USERTABLE
            + "' OR  TABLENAME = '" + USERINFOTABLE
            + "' OR  TABLENAME = '" + ROLETABLE + "'";
    private static final String HAS_ADMINISTRATOR = " select GROUPID from " + ROLETABLE
            + " WHERE GROUPID = 'ADMINISTRATORS' ";
    private String SIGNUPTABLE = "SIGNUPTABLE";
    private String GET_SIGNUPKEY = "SELECT SIGNUPKEY FROM SIGNUPTABLE WHERE USERID=?";
    private String DELETE_SIGNUPKEY = "DELETE FROM SIGNUPTABLE WHERE USERID=?";
    private String ADD_USER_SIGNUP_QUERY = "insert into SIGNUPTABLE (USERID) "
            + " values (?)";
}
