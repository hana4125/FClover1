DROP TABLE GOODS;

truncate TABLE GOODS;

-- FK 설정해야 함
CREATE TABLE GOODS
(
    GOODS_NO            bigint AUTO_INCREMENT PRIMARY KEY ,
    SELLER_NO           bigint not null, #-판매자아이디
    CATE_NO             INT, #--카테고리 번호
    GOODS_NAME          VARCHAR(50), #--상품명
    GOODS_CONTENT       VARCHAR(150), #--상품 설명
    GOODS_PRICE         INT not null, #--상품 가격
    GOODS_WRITER        VARCHAR(20) not null , #--상품 저자
    WRITER_CONTENT      VARCHAR(150),
    GOODS_CREATE_AT     DATE, #--상품 발행일
    GOODS_COUNT         INT, #--상품 총 판매수량 (베스트/스테디셀러 확인위해 필요한 값)
    GOODS_DATE          TIMESTAMP DEFAULT CURRENT_TIMESTAMP, #--상품 등록일
    GOODS_PAGECOUNT     INT,
    GOODS_BOOKSIZE      VARCHAR(10),
    UPDATE_AT           DATE,
    FULLTEXT INDEX ft_goods_idx (GOODS_NAME, GOODS_WRITER, GOODS_CONTENT) WITH PARSER ngram,
    FULLTEXT INDEX ft_goods_name_idx (GOODS_NAME) WITH PARSER ngram,
    FULLTEXT INDEX ft_goods_writer_idx (GOODS_WRITER) WITH PARSER ngram
) ENGINE=InnoDB;

insert into GOODS values(1,1, 1,'GoodsName1','GoodsContent1',
                         20000,'writer1', 'writerContent1',sysdate(),
                         1000, CURRENT_TIMESTAMP(), 300, 1,
                          current_timestamp());
insert into GOODS values(2,2, 1,'GoodsName2','GoodsContent2',
                         30000,'writer2', 'writerContent2', sysdate(),
                         2000, CURRENT_TIMESTAMP(), 350, 2,
                          current_timestamp());

insert into GOODS values(3,2, 2,'GoodsName3','GoodsContent3',
                         30000,'writer2', 'writerContent2', sysdate(),
                         3000, CURRENT_TIMESTAMP(), 350, 2,
                         current_timestamp());

insert into GOODS values(4,2, 2,'GoodsName4','GoodsContent4',
                         30000,'writer2', 'writerContent2', sysdate(),
                         4000, CURRENT_TIMESTAMP(), 400, 2,
                         current_timestamp());

insert into GOODS values(5,2, 2,'GoodsName5','GoodsContent5',
                         35000,'writer3', 'writerContent3', sysdate(),
                         5000, CURRENT_TIMESTAMP(), 400, 2,
                         current_timestamp());

insert into GOODS values(6,2, 2,'GoodsName6','GoodsContent6',
                         35000,'writer3', 'writerContent3', sysdate(),
                         6000, CURRENT_TIMESTAMP(), 300, 2,
                         current_timestamp());

insert into GOODS values(7,1, 3,'GoodsName7','GoodsContent7',
                         20000,'writer1', 'writerContent1',sysdate(),
                         7000, CURRENT_TIMESTAMP(), 300, 1,
                         current_timestamp());
insert into GOODS values(8,2, 3,'GoodsName8','GoodsContent8',
                         30000,'writer2', 'writerContent2', sysdate(),
                         8000, CURRENT_TIMESTAMP(), 350, 2,
                         current_timestamp());

insert into GOODS values(9,2, 3,'GoodsName9','GoodsContent9',
                         35000,'writer2', 'writerContent2', sysdate(),
                         9000, CURRENT_TIMESTAMP(), 350, 2,
                         current_timestamp());

insert into GOODS values(10,2, 4,'GoodsName10','GoodsContent10',
                         20000,'writer2', 'writerContent2', sysdate(),
                         10000, CURRENT_TIMESTAMP(), 400, 2,
                         current_timestamp());

insert into GOODS values(11,2, 4,'GoodsName11','GoodsContent11',
                         35000,'writer3', 'writerContent3', sysdate(),
                         11000, CURRENT_TIMESTAMP(), 400, 2,
                         current_timestamp());

insert into GOODS values(12,2, 4,'GoodsName12','GoodsContent12',
                         35000,'writer3', 'writerContent3', sysdate(),
                         12000, CURRENT_TIMESTAMP(), 300, 2,
                         current_timestamp());


commit ;

select * from goods;

SELECT
    goods_no,
    goods_name,
    goods_writer,
    goods_date
FROM goods
ORDER BY
    goods_date DESC
LIMIT 100;



