CREATE TABLE USERS
(
    num int primary key auto_increment,
    id varchar(20) not null unique,
    password varchar(20) not null,
    name varchar(10) not null,
    email varchar(20) not null,
    phone varchar(20) not null
);

