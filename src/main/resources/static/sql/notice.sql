
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

update notice
set noti_Title = '제목',
    noti_Name = '이름',
    noti_Content='내용'
where noti_No = 1;