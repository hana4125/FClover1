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
    GOODS_CREATE_AT     DATE, #--상품발행일
    GOODS_COUNT         INT, #--상품 총 판매수량 (베스트/스테디셀러 확인위해 필요한 값)
    GOODS_DATE          TIMESTAMP DEFAULT CURRENT_TIMESTAMP, #--상품 등록일
    GOODS_PAGECOUNT     INT,
    GOODS_BOOKSIZE      VARCHAR(10),
    UPDATE_AT           DATE,
    FULLTEXT INDEX ft_goods_idx (GOODS_NAME, GOODS_CONTENT, GOODS_WRITER) WITH PARSER ngram
) ENGINE=InnoDB;

insert into GOODS values(1,1, 1,'GoodsName1','GoodsContent1',
                         20000,'writer1', 'writerContent1',sysdate(),
                         1000, CURRENT_TIMESTAMP(), 300, 1,
                          current_timestamp());
insert into GOODS values(2,2, 1,'GoodsName2','GoodsContent2',
                         30000,'writer2', 'writerContent2', sysdate(),
                         2000, CURRENT_TIMESTAMP(), 350, 2,
                          current_timestamp());
commit ;

select * from goods;

delete from goods where goods_no=1;

