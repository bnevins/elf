echo deploying AuthAdmin
pushd ..\elf-web\AuthAdmin
call mvn clean install
cd target
call asadmin deploy --force AuthAdmin.war
popd


