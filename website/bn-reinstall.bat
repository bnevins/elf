echo Reinstalling GlassFish
pushd \bin
if exist glassfish3 rd glassfish3 /s/q
if exist glassfish3 goto err
setlocal

rem set the_zip=\gf\v3\distributions\glassfish\target\glassfish.zip
rem set the_zip=c:\incoming\glassfish-3.1.zip
rem set the_zip=E:\glassfish.zip
set the_zip=c:\incoming\ogs-3.1.2.2.zip
jar xvf %the_zip%
endlocal

popd
goto :EOF

:err
echo   ERROR

