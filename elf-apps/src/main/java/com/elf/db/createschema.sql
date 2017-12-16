create table USERTABLE(
        USERID varchar(32) NOT NULL CONSTRAINT USER_PK PRIMARY KEY ,
        PASSWORD varchar(64) NOT NULL
        );
create table GROUPTABLE(
        USERID varchar(32) NOT NULL, 
        GROUPID varchar(32) NOT NULL,
        CONSTRAINT GROUP_FK PRIMARY KEY(USERID, GROUPID),
        CONSTRAINT USER_FK FOREIGN KEY(USERID) REFERENCES USERTABLE(USERID)
            ON DELETE CASCADE ON UPDATE RESTRICT
    );
create table USERINFOTABLE(
            USERID varchar(32) NOT NULL,
            FIRSTNAME varchar(32), 
            LASTNAME varchar(32), 
            EMAIL varchar(100),
            CONSTRAINT USERINFO_FK FOREIGN KEY(USERID) REFERENCES USERTABLE(USERID)
            ON DELETE CASCADE ON UPDATE RESTRICT
);
create table SIGNUPTABLE (
        USERID          varchar(32) NOT NULL, 
        SIGNUPKEY  int GENERATED ALWAYS AS IDENTITY(START WITH 1371918),
        CONSTRAINT USERVALID_FK FOREIGN KEY(USERID) REFERENCES USERTABLE(USERID)
            ON DELETE CASCADE ON UPDATE RESTRICT
);
insert into USERTABLE(USERID,PASSWORD) 
    values ('admin', '21232f297a57a5a743894a0e4a801fc3');
insert into GROUPTABLE(USERID,GROUPID) values ('admin', 'USERS');
insert into GROUPTABLE(USERID,GROUPID) values ('admin', 'ADMINISTRATORS');
insert into USERINFOTABLE(USERID,FIRSTNAME,LASTNAME,EMAIL) values ('admin', 'delete or edit me', 'password is admin', '');
insert into SIGNUPTABLE(USERID) values ('admin');

insert into USERTABLE(USERID,PASSWORD) 
    values ('admin256', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');
insert into GROUPTABLE(USERID,GROUPID) values ('admin256', 'USERS');
insert into GROUPTABLE(USERID,GROUPID) values ('admin256', 'ADMINISTRATORS');
insert into USERINFOTABLE(USERID,FIRSTNAME,LASTNAME,EMAIL) values ('admin256', 'delete or edit me', 'password is admin', '');
insert into SIGNUPTABLE(USERID) values ('admin256');

