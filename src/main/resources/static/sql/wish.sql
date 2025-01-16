CREATE TABLE WISH
(
    WISH_NO     bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_no   bigint, #구매자 아이디
    GOODS_NO    BIGINT NOT NULL #상품 번호
);

commit;

drop table wish;

select * from wish;


