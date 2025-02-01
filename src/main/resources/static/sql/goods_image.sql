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

