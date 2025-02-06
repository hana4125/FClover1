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

SELECT *
FROM (
         SELECT
             ROW_NUMBER() OVER (ORDER BY d.deli_date DESC) AS no,
        d.deli_no AS deliNo,
        -- 주문일시는 현재 날짜/시간을 반환하도록 변경
        NOW() AS orderDate,
        d.deli_status AS deliStatus,
        g.GOODS_NAME AS goodsName,
        d.deli_quan AS quantity,
        d.deli_date AS deliveryDate
         FROM GOODS g
             LEFT JOIN delivery d ON g.GOODS_NO = d.inven_goods_no

         WHERE 1=1

     ) AS temp
ORDER BY temp.orderDate DESC
    LIMIT 10 OFFSET 0;

select *
from goods g join delivery
on g.goods_no = delivery.inven_goods_no
