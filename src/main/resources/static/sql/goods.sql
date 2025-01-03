create table goods (
    goods_no        INT             primary key,
    seller_id       VARCHAR(20)               ,
    cate_no         INT                       ,
    goods_name      VARCHAR(20)     not null   ,
    goods_content   VARCHAR(100)    ,
)