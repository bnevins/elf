connect 'jdbc:derby:/junk;create=true';

DROP TABLE TODO_ITEMS;

drop table usertable;
create table usertable ( USERID varchar(32) primary key, col01 varchar(64));

CREATE TABLE TODO_ITEMS (
        ITEM_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT TODO_ITEM_PK PRIMARY KEY,
        DESCRIPTION VARCHAR(64) NOT NULL,
        ASSIGNEE VARCHAR(32) NOT NULL,
        ASSIGNER VARCHAR(32),
        OPENED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CLOSED_DATE TIMESTAMP
);
ALTER TABLE TODO_ITEMS ADD CONSTRAINT "ASSIGNEE_FK" FOREIGN KEY(ASSIGNEE) REFERENCES USERTABLE(USERID) on delete cascade on update restrict;
ALTER TABLE TODO_ITEMS ADD CONSTRAINT "ASSIGNER_FK" FOREIGN KEY(ASSIGNER) REFERENCES USERTABLE(USERID) on delete cascade on update restrict;




insert into usertable values('byron', 'xyz');
insert into usertable values('steph', 'xyz');
insert into usertable values('diana', 'xyz');
insert into todo_items(description, assignee, assigner) values ('todo001', 'byron', 'byron');
insert into todo_items(description, assignee, assigner) values ('todo002', 'byron', 'steph');
insert into todo_items(description, assignee, assigner) values ('todo003', 'steph', 'byron');
insert into todo_items(description, assignee, assigner) values ('todo004', 'byron', 'diana');
insert into todo_items(description, assignee, assigner) values ('todo005', 'diana', 'byron');
insert into todo_items(description, assignee, assigner) values ('todo006', 'diana', 'steph');
insert into todo_items(description, assignee, assigner) values ('todo007', 'steph', 'diana');
