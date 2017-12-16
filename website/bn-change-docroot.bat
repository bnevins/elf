echo *** setting docroot to c:\docroot

call asadmin create-jvm-options -Dcom.sun.aas.docroot=/docroot
call asadmin set configs.config.server-config.http-service.virtual-server.server.docroot=${com.sun.aas.docroot}

