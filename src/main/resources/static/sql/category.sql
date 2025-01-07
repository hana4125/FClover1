-- 임시 카테고리 테이블

CREATE TABLE category
(
    cate_no int primary key not null,
    cate_name varchar(20)
);

insert into category (cate_no, cate_name) values
(1, '소설'), (2, '시/에세이'), (3, '인문'),
(4, '가정/육아'), (5, '요리'),(6, '건강'),
(7, '취미/실용/스포츠'), (8, '경제/경영'),
(9, '자기계발'), (10, '정치/사회'),
(11, '역사/문화'), (12, '종교'),
(13, '예술/대중문화'), (14, '중/고등참고서'),
(15,'기술/공학'), (16, '외국어'),
(17, '과학'), (18, '취업/수험서'),
(19, '여행'), (20, '컴퓨터/IT'),
(21, '잡지'), (22, '청소년'),
(23, '초등참고서'), (24,'유아(0~7세)'),
(25, '어린이(초등)'), (26, '만화'),
(27, '대학교재'), (28, '한국소개도서'),
(29, '교보오리지널');

DELETE FROM category;

drop table category;

select * from category;

commit;

create schema fClover;




