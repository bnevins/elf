echo list pools and resources
@echo off
echo ======================== jdbc pools:  ====================
call asadmin list-jdbc-connection-pools
echo ======================== jdbc resources:  ====================
call asadmin list-jdbc-resources
