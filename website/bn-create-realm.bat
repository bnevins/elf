echo create realm
call asadmin create-auth-realm --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm --property jaas-context=jdbcRealm:datasource-jndi=jdbc/bnevins:user-table=USERTABLE:user-name-column=USERID:password-column=PASSWORD:group-table=GROUPTABLE:group-name-column=GROUPID:db-user=APP:db-password=APP:digest-algorithm=SHA-256 bnevins
