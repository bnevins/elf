1. set the zipfile location in bn-reinstall.bat
2. You have to setup Derby or JavaDB as a separate install -- easier to access DB, etc.  
So do that BEFORE running the main script.

Get windos resource kit -- 
o

 Running Derby Server as Windows Service
The Derby Server is started via a batch program. In an server environment this batch program should be automatically started if the server is rebooted / started. The windows program "srvmgr" can be used for this purpose. For details on the tool please check the official documentation; the following will give a description how this can be used for Apache Derby. Install "srvmgr" and remember the installation path.

We will call our service "ApacheDerby" and the batch file is located under "C:\db-derby\bin\startNetworkServer.bat" In the command line register a service via:

# This assumes the "srvmgr" tools are installed in c:\Windows\system32\
instsrv ApacheDerby c:\Windows\system32\srvany.exe 
You should receive a success message.

Tip
If something does wrong and you want to remove an installed service, you can use.
# This will remove a service installed with the name ApacheDerby
instsrv ApacheDerby REMOVE 
Run Regedt32.exe and locate the following subkey HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\ApacheDerby

From the Edit menu, select New -> Key and add a key named "Parameters".

Select the "Parameters" key, that you have just created: From the Edit menu, select New -> String Value. Maintain the following values.

Value Name: Application
Data Type : REG_SZ
String : C:\db-derby\bin\startNetworkServer.bat

Value Name: AppDirectory
Data Type : REG_SZ
String : C:\db-derby\bin\

Value Name: AppParameters
Data Type : REG_SZ
String : -h 0.0.0.0 
Now start/adjust the service in the Windows services control panel
