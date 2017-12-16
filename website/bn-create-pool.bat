echo create pool
call asadmin create-jdbc-connection-pool  --restype=javax.sql.DataSource  --datasourceclassname=org.apache.derby.jdbc.ClientDataSource --property DatabaseName=bnevins:PortNumber=1527:serverName=localhost:connectionAttributes=\;create\=true:Password=APP:User=APP bnevins
