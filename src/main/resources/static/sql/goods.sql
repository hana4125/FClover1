drop table goods;
create Table goods(
  goods_no int primary key auto_increment,
  seller_id varchar(20), -- 판매자 아이디
  cate_no int,
  goods_name varchar(20), -- 상품명
  goods_content varchar(100), -- 상품설명
  goods_price int, -- 상품가격
  goods_writer varchar(10), -- 상품 저자
  seller_company varchar(10), -- 출판사
  goods_create_at date, -- 상품 발행일
  goods_image varchar(255),-- 상품이미지 링크
  goods_count int, -- 상품 총 판매수량
  goods_date datetime, -- 상품 등록일
  goods_pageCount int, -- 상품 페이지수
  goods_bookSize varchar(10), -- 상품 크기
  writer_content varchar(100), -- 저자 설명
  update_at date -- 수정 일자
)