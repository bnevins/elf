echo deploying Web Art
pushd ..\elf-web\WebArt
call mvn clean install
cd target
call asadmin deploy --force WebArt.war
popd


