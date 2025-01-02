
CREATE TABLE NOTICE (
NOTI_NUM INT,               -- 공지사항 번호
NOTI_TITLE VARCHAR(300),  -- 공지사항 제목
NOTI_NAME VARCHAR(30),      -- 작성자
NOTI_CONTENT VARCHAR(4000), -- 내용
NOTI_RE_REF INT,            -- 답변 글 작성시 참조되는 글 번호
NOTI_RE_LEV INT,            -- 답변 글의 깊이
NOTI_RE_SEQ INT,            -- 답변 글의 순서
NOTI_DATE DATETIME,         -- 글의 작성 날짜
PRIMARY KEY (NOTI_NUM)
);

select * from NOTICE;

drop table NOTICE;