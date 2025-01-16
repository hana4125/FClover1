CREATE TABLE cart
(
    cart_no   bigint auto_increment PRIMARY KEY,
    member_no bigint,
    goods_no  bigint,
    quantity int
);

commit;

drop table cart;

select *
from cart;


