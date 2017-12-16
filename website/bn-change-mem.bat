echo *** setting -Xmx from 512 to 1024

call asadmin delete-jvm-options \-Xmx512m
call asadmin create-jvm-options \-Xmx1024m
call asadmin delete-jvm-options --target default-config \-Xmx512m
call asadmin create-jvm-options --target default-config \-Xmx1024m
