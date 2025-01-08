CREATE TABLE GOODS
(
    GOODS_NO            bigint NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    GOODS_ISBN          INT, #상품번호(isbn)
    SELLER_ID           VARCHAR(20), #-판매자아이디
    CATE_NO             INT, #--카테고리 번호
    GOODS_NAME          VARCHAR(20), #--상품명
    GOODS_CONTENT       VARCHAR(100), #--상품 설명
    GOODS_PRICE         INT not null, #--상품 가격
    GOODS_WRITER        VARCHAR(10) not null , #--상품 저자
    SELLER_COMPANY      VARCHAR(10) not null , #-- 출판사
    GOODS_CREATE_AT     DATE, #--상품발행일
    GOODS_IMAGE         VARCHAR(255), #--상품이미지 링크
    GOODS_COUNT         INT, #--상품 총 판매수량 (베스트/스테디셀러 확인위해 필요한 값)
    GOODS_DATE          TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP #--상품 등록일
);

insert into GOODS values(1,123111,'test1',1,'GoodsName1',
                         'GoodsContent1',1000,'writer1',
                         'company1',sysdate(),null,1,current_timestamp() );
insert into GOODS values(2,123222,'test2',1,'GoodsName2',
                         'GoodsContent2',1000,'writer2',
                         'company2',sysdate(),null,2,current_timestamp() );
insert into GOODS values(3,123333,'test3',1,'GoodsName3',
                         'GoodsContent3',1000,'writer1',
                         'company3',sysdate(),null,3,current_timestamp() );
insert into GOODS values(4,123444,'test4',1,'GoodsName4',
                         'GoodsContent4',1000,'writer4',
                         'company4',sysdate(),null,4,current_timestamp() );
insert into GOODS values(5,123555,'test5',1,'GoodsName5',
                         'GoodsContent5',1000,'writer5',
                         'company5',sysdate(),null,1,current_timestamp() );

commit ;

drop table goods;

select * from goods;

