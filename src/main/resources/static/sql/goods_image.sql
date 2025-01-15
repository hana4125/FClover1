drop table goods_image;
create Table goods_image(
  image_no int primary key auto_increment,
  goods_no int not null ,
  goods_image_name varchar(50), -- 이미지 저장 파일명
  goods_url varchar(255),
  is_main char check(is_main IN('M', 'S')),
  foreign key (goods_no) references goods(goods_no)
);
select * from goods_image;