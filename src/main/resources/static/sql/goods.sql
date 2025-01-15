CREATE TABLE GOODS
(
    GOODS_NO            bigint NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    GOODS_ISBN          INT, #상품번호(isbn)
    SELLER_ID           VARCHAR(20), #-판매자아이디
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

insert into GOODS values(1,123111,'test1',1,'GoodsName1',
                         'GoodsContent1',2000,'writer1',
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

insert into GOODS values(6,123666,'test6',1,'GoodsName6',
                         'GoodsContent1',1000,'writer6',
                         'company1',sysdate(),null,1,current_timestamp() );
insert into GOODS values(7,123777,'test7',1,'GoodsName7',
                         'GoodsContent2',1000,'writer7',
                         'company2',sysdate(),null,2,current_timestamp() );
insert into GOODS values(8,123888,'test8',1,'GoodsName8',
                         'GoodsContent3',1000,'writer8',
                         'company3',sysdate(),null,3,current_timestamp() );
insert into GOODS values(9,123999,'test9',1,'GoodsName9',
                         'GoodsContent4',1000,'writer9',
                         'company4',sysdate(),null,4,current_timestamp() );
insert into GOODS values(10,123000,'test10',1,'GoodsName10',
                         'GoodsContent5',1000,'writer10',
                         'company5',sysdate(),null,1,current_timestamp() );

insert into GOODS values(11,123666,'test11',1,'GoodsName11',
                         'GoodsContent1',1000,'writer6',
                         'company1',sysdate(),null,1,current_timestamp() );
insert into GOODS values(12,123777,'test12',1,'GoodsName12',
                         'GoodsContent2',1000,'writer7',
                         'company2',sysdate(),null,2,current_timestamp() );
insert into GOODS values(13,123888,'test13',1,'GoodsName13',
                         'GoodsContent3',1000,'writer8',
                         'company3',sysdate(),null,3,current_timestamp() );
insert into GOODS values(14,123999,'test14',1,'GoodsName14',
                         'GoodsContent4',1000,'writer9',
                         'company4',sysdate(),null,4,current_timestamp() );
insert into GOODS values(15,123000,'test15',1,'GoodsName15',
                         'GoodsContent5',1000,'writer10',
                         'company5',sysdate(),null,1,current_timestamp() );

insert into GOODS values(16,123666,'test16',1,'GoodsName16',
                         'GoodsContent1',1000,'writer6',
                         'company1',sysdate(),null,1,current_timestamp() );
insert into GOODS values(17,123777,'test17',1,'GoodsName17',
                         'GoodsContent2',1000,'writer7',
                         'company2',sysdate(),null,2,current_timestamp() );
insert into GOODS values(18,123888,'test18',1,'GoodsName18',
                         'GoodsContent3',1000,'writer8',
                         'company3',sysdate(),null,3,current_timestamp() );
insert into GOODS values(19,123999,'test19',1,'GoodsName19',
                         'GoodsContent4',1000,'writer9',
                         'company4',sysdate(),null,4,current_timestamp() );
insert into GOODS values(20,123000,'test20',1,'GoodsName20',
                         'GoodsContent5',1000,'writer10',
                         'company5',sysdate(),null,1,current_timestamp() );

insert into GOODS values(21,123666,'test21',1,'GoodsName21',
                         'GoodsContent1',1000,'writer6',
                         'company1',sysdate(),null,1,current_timestamp() );
insert into GOODS values(22,123777,'test22',1,'GoodsName22',
                         'GoodsContent2',1000,'writer7',
                         'company2',sysdate(),null,2,current_timestamp() );
insert into GOODS values(23,123888,'test23',1,'GoodsName23',
                         'GoodsContent3',1000,'writer8',
                         'company3',sysdate(),null,3,current_timestamp() );
insert into GOODS values(24,123999,'test24',1,'GoodsName24',
                         'GoodsContent4',1000,'writer9',
                         'company4',sysdate(),null,4,current_timestamp() );
insert into GOODS values(25,123000,'test25',1,'GoodsName25',
                         'GoodsContent5',1000,'writer10',
                         'company5',sysdate(),null,1,current_timestamp() );

commit ;

drop table goods;

select * from goods;

delete from goods where goods_no=1;

