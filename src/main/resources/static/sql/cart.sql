CREATE TABLE CART
(
    CART_NO             bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_no           int, #구매자 키
    GOODS_NO            BIGINT, #상품 키
    CART_QUANTITY       INT #장바구니에 담은 수량(1개 상품에 대해)
);

commit;

drop table cart;

select * from cart;


