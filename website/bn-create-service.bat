echo *** setting up Windows Service
call asadmin stop-domain
call asadmin create-service bnevins
sc start bnevins



