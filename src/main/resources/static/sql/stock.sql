CREATE TABLE `stock` (
    #   stock_no 로 pk 수정
                               `stock_no` bigint unsigned NOT NULL AUTO_INCREMENT,
                               `goods_no` int not NULL,
                               `goods_code` int,
                                #초기수량
                                `initial_stock` int not null ,
                                #현재수량
                                `product_stock` int DEFAULT NULL,
                                `isSoldOut` varchar(3),
                               PRIMARY KEY (`stock_no`),
                               UNIQUE KEY `id` (`stock_no`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;


select *from stock;


drop table stock;

insert into stock (goods_no, goods_code, initial_stock, product_stock, isSoldOut) values(499863,1234,100, 20,'N');

commit;

update stock
set stock_no = 3
where stock_no = 4;