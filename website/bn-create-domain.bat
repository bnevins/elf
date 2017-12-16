echo delete domain1, create domain: bnevins
echo ***********   IMPORTANT PAY ATTENTION -- the username is bnevins,  enter password !!!!!
echo ***********   IMPORTANT PAY ATTENTION -- the username is bnevins,  enter password !!!!!
echo ***********   IMPORTANT PAY ATTENTION -- the username is bnevins,  enter password !!!!!
call asadmin delete-domain domain1
call asadmin create-domain --user bnevins --savelogin=true --keytooloptions CN=bnevins.com bnevins
