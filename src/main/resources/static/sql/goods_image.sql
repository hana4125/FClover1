create Table goods_image(
  image_num int primary key auto_increment,
  goods_no int,
  img_ori_name varchar(255),-- 이미지 본래 파일명
  img_save_name varchar(255) -- 이미지 저장 파일명
)