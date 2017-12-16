echo deploying Weight Tool
pushd ..\elf-web\WeightTool
call mvn clean install
cd target
call asadmin deploy --force WeightTool.war
popd


