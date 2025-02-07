CREATE TABLE delivery
(
    deli_no BIGINT primary key AUTO_INCREMENT,
    payments_no BIGINT,
    inven_goods_no BIGINT,
    deli_Type varchar(20),
    deli_quan int,
    deli_content varchar(200),
    deli_date datetime,
    deli_cus_name varchar(100),
    deli_status varchar(10)
);

commit;

drop table delivery;


select * from delivery;

insert into delivery values
(4,5,1, null,1,
 null, now(),null,'배송완료');

insert into delivery values
    (5,6,1, null,1,
     null, now(),null,'배송6');

insert into delivery values
    (6,7,1, null,1,
     null, now(),null,'배송7');

