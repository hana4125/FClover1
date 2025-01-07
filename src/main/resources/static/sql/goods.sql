-- 임시 카테고리 테이블

CREATE TABLE goods
(
    goods_no int primary key not null,
    seller_id varchar(20),
    cate_no int,
    goods_name varchar(20),
    goods_content varchar(100),
    goods_price int,
    goods_writer varchar(10),
    seller_company varchar(10),
    goods_create_at date,
    goods_image varchar(255),
    goods_count int,
    goods_date datetime
);

insert into goods (goods_no, seller_id, cate_no, goods_name, goods_content, goods_price, goods_writer, seller_company
                     ,goods_create_at, goods_image, goods_count, goods_date) values
(1, 'cjh971202', 1, '구미호카페', '재밌어요', 25000, '박현숙',
 '특별한 서재', '2025-01-07', '', 100, '2025-01-01 00:00:00');

DELETE FROM goods;

drop table goods;

select * from goods;

commit;

create schema fClover;




