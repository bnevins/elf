echo deploying Weight Tool
pushd ..\elf-web\WeightTool
call mvn clean install
cd target
asadmin deploy --force WeightTool.war
popd


