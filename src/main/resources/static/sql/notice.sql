
CREATE TABLE NOTICE (
noti_No INT,
noti_Title VARCHAR(300),
noti_Name VARCHAR(30),
noti_Content VARCHAR(4000),
noti_Date DATETIME,
PRIMARY KEY (noti_No)
);

select * from NOTICE;

drop table NOTICE;

TRUNCATE TABLE NOTICE;

commit;

