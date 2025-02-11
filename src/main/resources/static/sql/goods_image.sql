drop table goods_image;
create Table goods_image(
  image_no BIGINT primary key auto_increment,
  goods_no BIGINT not null ,
  goods_image_name varchar(50), -- 이미지 저장 파일명
  goods_url varchar(255),
  is_main char check(is_main IN('M', 'S')),
  foreign key (goods_no) references goods(goods_no)
);
select * from goods_image;

update goods_image
    set goods_no = '500003'
where goods_no = 12;

update goods_image
set goods_no = '500004'
where goods_no = 11;

commit;

SELECT
    image_no          AS imageNo,
    goods_no          AS goodsNo,
    goods_image_name  AS goodsImageName,
    goods_url         AS goodsUrl,
    is_main           AS isMain
FROM goods_image
WHERE goods_no = 500003
  AND is_main = 'M';

SELECT
    constraint_name
FROM
    information_schema.key_column_usage
WHERE
    table_schema = 'masterDB2' -- 데이터베이스 이름
  AND table_name = 'goods_image'
  AND column_name = 'goods_no';

ALTER TABLE goods_image
    ADD CONSTRAINT fk_goods
        FOREIGN KEY (goods_no) REFERENCES goods(goods_no)
            ON DELETE CASCADE;