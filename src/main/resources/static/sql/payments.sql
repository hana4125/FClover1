CREATE TABLE `payments` (

    #payments_no 로 pk 수정
    ##구매수량 컬럼 추가
#`payment_quantity` bigint DEFAULT NULL,
##상품 번호 컬럼 추가
# `goods_no` int NOT NULL,
##쿠폰 할인 금액 추가
# `coupon_discount` int NOT NULL,
                            `payments_no` bigint NOT NULL AUTO_INCREMENT,
                            `seller_id` bigint DEFAULT NULL,
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


alter table payments
add column goods_no bigint not null after order_id;

alter table payments
    add column payment_quantity bigint not null after order_id;

commit;

select* from payments;

update payments
set payment_quantity=80, goods_no=499863
where payments_no = 1;