CREATE TABLE WISH
(
    WISH_NO  bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id   VARCHAR(20), #구매자 아이디
    GOODS_NO BIGINT NOT NULL, #상품 번호
    GOODS_IMAGE VARCHAR(255) #상품 이미지
);

commit;

drop table wish;

select * from wish;


