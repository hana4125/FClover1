-- 임시 회원 테이블

CREATE TABLE member
(
    num int primary key auto_increment,
    member_id varchar(20) not null unique,
    password varchar(60) not null,
    name varchar(10) not null,
    email varchar(20) not null,
    phone varchar(20) not null,
    auth varchar(20) not null
);

