create table delivery
(
    num                   int primary key auto_increment,
    member_id             varchar(20) not null,
    shipping_address_name varchar(50) not null,
    receiver              varchar(20) not null,
    phone                 varchar(20) not null,
    address               varchar(50) not null
);