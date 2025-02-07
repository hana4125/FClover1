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
    ROW_NUMBER() OVER (ORDER BY p.payment_date DESC) as no,
    p.payments_no AS orderNo,
    p.payment_date AS orderDate,
    d.deli_status AS deliStatus,
    g.GOODS_NAME AS goodsName,
    d.deli_quan AS quantity,
    m.member_id AS customerName,
    d.deli_date AS deliveryDate
FROM payments p
         LEFT JOIN delivery d ON p.payments_no = d.payments_no
         LEFT JOIN goods g ON d.inven_goods_no = g.GOODS_NO
         LEFT JOIN member m ON p.user_id = m.member_id
    ORDER BY p.payment_date DESC;

-- 1. payments(기준) & delivery
SELECT
    ROW_NUMBER() OVER (ORDER BY p.payment_date DESC) as no,
    p.payments_no AS orderNo,
    p.payment_date AS orderDate,
    d.deli_status AS deliStatus,
    d.deli_quan AS quantity,
    d.deli_date AS deliveryDate
FROM payments p
          JOIN delivery d ON p.payments_no = d.payments_no
ORDER BY p.payment_date DESC

-- 2. 1번 결과에 goods 추가
SELECT
    ROW_NUMBER() OVER (ORDER BY p.payment_date DESC) as no,
    p.payments_no AS orderNo,
    p.payment_date AS orderDate,
    g.GOODS_NAME AS goodsName,
    d.deli_status AS deliStatus,
    d.deli_quan AS quantity,
    d.deli_date AS deliveryDate
FROM payments p
         JOIN delivery d ON p.payments_no = d.payments_no
         JOIN goods g ON d.inven_goods_no = g.GOODS_NO
ORDER BY p.payment_date DESC

-- 3. 2번 결과에 member 추가
SELECT
    ROW_NUMBER() OVER (ORDER BY p.payment_date DESC) as no,
    p.payments_no AS orderNo,
    p.payment_date AS orderDate,

    d.deli_status AS deliStatus,
    d.deli_quan AS quantity,

    d.deli_date AS deliveryDate
from (select *
FROM payments p
where p.seller_id = '16') p # p.seller_id 값은 seller 테이블의 seller_no 값이 들어가야 함!!
         JOIN delivery d ON p.payments_no = d.payments_no

ORDER BY p.payment_date DESC
