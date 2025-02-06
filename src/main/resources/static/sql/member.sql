CREATE TABLE member
(
    member_no       BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id       VARCHAR(100) NOT NULL UNIQUE,
    password        VARCHAR(100),
    name            VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL,
    birthdate       VARCHAR(20),
    phone_number    VARCHAR(20),
    gender          VARCHAR(10),
    auth            VARCHAR(20)  NOT NULL,
    profile_picture VARCHAR(255) DEFAULT NULL,
    created_at      DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME
);


commit;

select * from member;

drop table member;

update member
set auth = 'ROLE_ADMIN'
where member_id = 'admin';

delete from member where member_id = 'userid';


SELECT
    p.payments_no AS paymentsNo,
    NOW() AS orderDate,
    d.deli_status AS deliStatus,
    g.GOODS_NAME AS goodsName,
    d.deli_quan AS quantity,
    m.member_id AS customerName,
    d.deli_date AS deliveryDate
FROM payments p
         LEFT JOIN delivery d ON p.payments_no = d.payments_no
         LEFT JOIN goods g ON d.inven_goods_no = g.GOODS_NO
         LEFT JOIN member m ON p.user_id = m.member_id;



