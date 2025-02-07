
CREATE TABLE notice (
noti_no INT,
noti_title VARCHAR(300),
noti_Name VARCHAR(30),
noti_content VARCHAR(4000),
noti_Date DATETIME,
PRIMARY KEY (noti_no)
);

select * from notice;

drop table notice;

commit;

ALTER TABLE notice
    ADD FULLTEXT (noti_title);

DELETE FROM notice WHERE noti_no = 1;







