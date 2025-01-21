CREATE TABLE `settlements` (
                               `settlements_no` bigint unsigned NOT NULL AUTO_INCREMENT,
                               `seller_id` int DEFAULT NULL,
                               `total_amount` decimal(15,2) NOT NULL,
                               `status` varchar(20) DEFAULT 'Pending',
                               `payment_date` date DEFAULT NULL,
                               `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                               `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (`settlements_no`),
                               UNIQUE KEY `id` (`settlements_no`)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;

commit;

select * from settlements;
