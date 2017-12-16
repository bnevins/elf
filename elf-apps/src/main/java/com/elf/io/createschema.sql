create table FILEINFO(
        PATH varchar(530) PRIMARY KEY,
        NAME varchar(100) NOT NULL,
        DATE BIGINT NOT NULL,
        DIGEST varchar(32)
        ORIGINAL_NAME varchar(100),
        TYPE varchar(20)
        );
create index digestindex on fileinfo(digest);
create index dateindex on fileinfo(date);