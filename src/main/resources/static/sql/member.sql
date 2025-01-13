CREATE TABLE member
(
    member_no         INT AUTO_INCREMENT PRIMARY KEY,
    member_id       VARCHAR(100)  NOT NULL UNIQUE,
    password        VARCHAR(100)         ,
    name            VARCHAR(20)  NOT NULL,
    email           VARCHAR(100)  NOT NULL,
    phone_number    VARCHAR(20)          ,
    auth            VARCHAR(20)  NOT NULL,
    profile_picture VARCHAR(255) DEFAULT NULL,
    created_at      DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME
);

commit;

drop table member;

update member
set auth = 'ROLE_ADMIN'
where member_id = 'admin';

select * from member;