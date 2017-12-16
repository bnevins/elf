DROP TABLE TODO_ITEMS;

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






