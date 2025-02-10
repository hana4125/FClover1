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




