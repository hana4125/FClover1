CREATE TABLE `payments` (
                            `payments_no` bigint NOT NULL AUTO_INCREMENT,
                            `partner_id` bigint DEFAULT NULL,
                            `user_id` varchar(50) NOT NULL,
                            `order_id` bigint NOT NULL,
                            `payment_amount` decimal(15,2) NOT NULL,
                            `payment_date` datetime NOT NULL,
                            `imp_uid` varchar(50) DEFAULT NULL,
                            `payment_method` varchar(50) DEFAULT NULL,
                            `merchant_uid` varchar(50) DEFAULT NULL,
                            `pg_provider` varchar(20) DEFAULT NULL,
                            `pg_type` varchar(20) DEFAULT NULL,
                            `pg_tid` varchar(50) DEFAULT NULL,
                            `status` varchar(20) DEFAULT NULL,
                            `card_name` varchar(20) DEFAULT NULL,
                            `card_number` varchar(50) DEFAULT NULL,
                            `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`payments_no`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;

select* from payments;]


select * from payments where user_id='test1';

commit;

select* from payments;