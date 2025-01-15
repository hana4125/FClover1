CREATE TABLE wish (
    wish_no bigint PRIMARY KEY,
    member_no bigint,
    goods_no bigint
);

commit;

drop table wish;

select * from wish;


