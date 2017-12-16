echo *************************************************************
echo ****   MEGA SETUP SCRIPT STARTED....                     ****
echo *************************************************************
rem @echo off
pause ****** make sure derby server is running !!!!!!
call bn-reinstall.bat
pause
rem bn-delete-service.bat
pause
call bn-create-domain.bat
pause
rem Note that this should be called BEFORE starting the domain!
call bn-deploy-utils.bat
pause
call bn-start-domain.bat
pause

rem added the --savelogin so this is no longer needed!
rem call bn-login.bat

call bn-create-pool.bat
pause
call bn-pinger.bat
pause
call bn-create-resource.bat
pause
call bn-create-realm.bat
pause
call bn-create-auth-tables.bat
pause
call bn-list.bat
pause
call bn-change-ports.bat
pause
call bn-change-mem.bat
pause
call bn-enable-secure.bat



rem call bn-change-docroot.bat





call bn-restart.bat
pause
call bn-deploy-authadmin.bat
pause
call bn-deploy-weighttool.bat
pause
call bn-deploy-webart.bat

