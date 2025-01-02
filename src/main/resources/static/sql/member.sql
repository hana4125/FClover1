
-- 임시 회원 테이블
CREATE TABLE member
(
    #num int auto_increment not null,
    mem_id varchar(20) not null,
    mem_pass varchar(50) not null,
    mem_name varchar(100) not null,
    mem_email varchar(100) not null,
    mem_phone varchar(20) not null,
    mem_profile varchar(255),
    mem_address varchar(200),
    mem_birth varchar(8),
    mem_gender varchar(1),
    created_at date,
    updated_at date,
    primary key(mem_id)
);

CREATE DATABASE FClover default CHARACTER SET UTF8;
SHOW DATABASES;


select * from member;
drop table member;
