CREATE TABLE `coupon` (
                          `coupon_id` bigint NOT NULL AUTO_INCREMENT,
                          `member_id` varchar(100) NOT NULL,
                          `coupon_name` varchar(50) NOT NULL,
                          `coupon_amount` bigint NOT NULL,
                          `coupon_created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                          `coupon_expire` timestamp NULL,
                          PRIMARY KEY (`coupon_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;


drop table coupon;

commit;

CREATE TRIGGER set_coupon_expire_before_insert
    BEFORE INSERT ON `coupon`
    FOR EACH ROW
BEGIN
    IF NEW.coupon_expire IS NULL THEN
        SET NEW.coupon_expire = DATE_ADD(NEW.coupon_created_at, INTERVAL 30 DAY);
    END IF;
END



