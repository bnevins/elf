alter table weight_items (add column PERSON varchar(32));
update weight_items set PERSON = 'bnevins';

alter table weight_items alter column PERSON not null;
