-- 임시 카테고리 테이블

CREATE TABLE category
(
    cate_num int not null,
    cate_name varchar(20)
);

insert into category (cate_num, cate_name) values
(1, '소설'), (2, '시/에세이'), (3, '인문'),
(4, '가정/육아'), (5, '요리'),(6, '종교'),
(7, '예술/대중문화'), (8, '중/고등참고서'),
(9,'기술/공학'), (10, '외국어'),
(11, '초등참고서'), (12,'유아(0~7세)'),
(13, '어린이(초등)'), (14, '만화'),
(15, '대학교재');
commit;



