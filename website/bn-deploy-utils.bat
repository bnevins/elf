echo deploying elf-gf.jar and elf-commons.jar
cd ..\elf-commons
call mvn clean install
copy target\elf-commons.jar "%GF_HOME%\lib"
cd ..\elf-gf
call mvn clean install
copy target\elf-gf.jar "%GF_HOME%\lib"
cd ..\website
dir "%GF_HOME%\lib\elf-gf.jar"

