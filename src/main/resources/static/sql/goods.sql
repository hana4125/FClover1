CREATE TABLE GOODS
(
    GOODS_NO            bigint NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    SELLER_NO           bigint, #-판매자아이디
    CATE_NO             INT, #--카테고리 번호
    GOODS_NAME          VARCHAR(20), #--상품명
    GOODS_CONTENT       VARCHAR(100), #--상품 설명
    GOODS_PRICE         INT not null, #--상품 가격
    GOODS_WRITER        VARCHAR(50) not null , #--상품 저자
    SELLER_COMPANY      VARCHAR(50) not null , #-- 출판사
    GOODS_CREATE_AT     DATE, #--상품발행일
    GOODS_COUNT         INT, #--상품 총 판매수량 (베스트/스테디셀러 확인위해 필요한 값)
    GOODS_DATE          TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP, #--상품 등록일
    GOODS_PAGECOUNT     INT,
    GOODS_BOOKSIZE      VARCHAR(10),
    WRITER_CONTENT      VARCHAR(100),
    UPDATE_AT           DATE,
    FULLTEXT KEY ft_index (GOODS_NAME, GOODS_CONTENT, GOODS_WRITER, SELLER_COMPANY) WITH PARSER ngram
) ENGINE=InnoDB;

insert into GOODS values(1,1, 1,'GoodsName1','GoodsContent1',
                         20000,'writer1', 'company1', sysdate(),
                         1000, CURRENT_TIMESTAMP(), 300, 1,
                         'writerContent1', current_timestamp());
commit ;

drop table goods;

select * from goods;

delete from goods where goods_no=1;

