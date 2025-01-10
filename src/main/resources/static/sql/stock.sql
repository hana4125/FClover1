CREATE TABLE `stock` (
                               `id` bigint unsigned NOT NULL AUTO_INCREMENT,
                               `goods_no` int not NULL,
                               `goods_code` int,
                                #초기수량
                                `initial_stock` int not null ,
                                #현재수량
                                `product_stock` int DEFAULT NULL,
                                `isSoldOut` varchar(3),
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `id` (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;


select *from stock;


drop table stock;

insert into stock (goods_no, goods_code, initial_stock, product_stock, isSoldOut) values(3,1234,100,100,'N');