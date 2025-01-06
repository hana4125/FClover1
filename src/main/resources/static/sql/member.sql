
-- 회원 테이블
CREATE TABLE member
(
    mem_num         INT AUTO_INCREMENT PRIMARY KEY,
    member_id       VARCHAR(20)  NOT NULL UNIQUE,
    password        VARCHAR(100) NOT NULL,
    name            VARCHAR(20)  NOT NULL,
    email           VARCHAR(20)  NOT NULL,
    phone    VARCHAR(20)  NOT NULL,
    auth            VARCHAR(20)  NOT NULL,
    profile_picture VARCHAR(255) DEFAULT NULL,
    created_at      DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME
);

CREATE DATABASE FClover default CHARACTER SET UTF8;
SHOW DATABASES;


select * from member;
drop table member;

SHOW CREATE TABLE comments;
delete from member;

update member
set auth = 'ROLE_ADMIN'
where auth = 'ROLE_MEMBER';



