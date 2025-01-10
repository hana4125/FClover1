CREATE TABLE `delivery` (
                            `deli_id` bigint NOT NULL AUTO_INCREMENT,
                            `order_id` LONG DEFAULT NULL,
                            `user_id` varchar(50) NOT NULL,
                            `deli_status` varchar(50) not null,
                            `deli_num` int,
                            `deli_company` varchar(50),
                            `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`deli_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;



select* from delivery;

drop table delivery;