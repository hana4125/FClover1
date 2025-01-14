CREATE TABLE CART
(
    CART_GOODS_NO       bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id              VARCHAR(20), #구매자 아이디
    CART_ID             INT, #카트 아이디
    GOODS_NO            BIGINT NOT NULL, #상품 키
    CART_QUAN           INT, #장바구니에 담은 수량(1개 상품에 대해)
    ADDED_AT            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP #담긴 일시
);

commit;

drop table cart;

select * from cart;


