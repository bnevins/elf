echo *** setting ports to 80 and 81

call asadmin set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-1.port=80
call asadmin set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-2.port=81
