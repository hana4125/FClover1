DROP TABLE goods;

truncate TABLE goods;

-- FK 설정해야 함
CREATE TABLE goods
(
    goods_no            bigint AUTO_INCREMENT PRIMARY KEY, #--상품번호
    cate_no             INT not null, #--카테고리 번호
    seller_no           bigint not null, #--판매자 번호
    company_name        VARCHAR(100) not null, #--판매자이름(출판사명)
    goods_name          VARCHAR(100) not null, #--상품명
    goods_content       VARCHAR(255) not null, #--상품 설명
    goods_price         INT not null, #--상품 가격
    goods_writer        VARCHAR(30) not null, #--상품 저자
    writer_content      VARCHAR(255) not null, #--저자 소개
    goods_create_at     DATE not null, #--상품 발행일
    goods_date          TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP, #--상품 등록일
    goods_pagecount     INT not null,
    goods_booksize      VARCHAR(10) not null,
    update_at           DATE DEFAULT NULL,
    permission          VARCHAR(50) NOT NULL DEFAULT 'Pending',

    -- 영어 검색용 생성 컬럼
    goods_text_en    TEXT GENERATED ALWAYS AS (
        CONCAT_WS(' ', goods_name, goods_writer, company_name, goods_content)
        ) STORED,

    -- 한글 검색용 Full Text Index (ngram 파서 사용)
    FULLTEXT INDEX ft_goods_idx (goods_name, goods_writer, company_name, goods_content) WITH PARSER ngram,
    FULLTEXT INDEX ft_goods_name_idx (goods_name) WITH PARSER ngram,
    FULLTEXT INDEX ft_goods_writer_idx (goods_writer) WITH PARSER ngram,
    FULLTEXT INDEX ft_goods_company_idx (company_name) WITH PARSER ngram,

    -- 영어 검색용 Index (기본 파서 사용)
    FULLTEXT INDEX ft_goods_en_idx (goods_text_en),

    INDEX idx_goods_date (goods_date)
) ENGINE=InnoDB;

# insert into goods values(1,1, 1,'GoodsName1','GoodsContent1',
#                          20000,'writer1', 'writerContent1',sysdate(),
#                          1000, CURRENT_TIMESTAMP(), 300, 1,
#                           current_timestamp());
# insert into goods values(2,2, 1,'GoodsName2','GoodsContent2',
#                          30000,'writer2', 'writerContent2', sysdate(),
#                          2000, CURRENT_TIMESTAMP(), 350, 2,
#                           current_timestamp());
#
# insert into goods values(3,2, 2,'GoodsName3','GoodsContent3',
#                          30000,'writer2', 'writerContent2', sysdate(),
#                          3000, CURRENT_TIMESTAMP(), 350, 2,
#                          current_timestamp());
#
# insert into goods values(4,2, 2,'GoodsName4','GoodsContent4',
#                          30000,'writer2', 'writerContent2', sysdate(),
#                          4000, CURRENT_TIMESTAMP(), 400, 2,
#                          current_timestamp());
#
# insert into goods values(5,2, 2,'GoodsName5','GoodsContent5',
#                          35000,'writer3', 'writerContent3', sysdate(),
#                          5000, CURRENT_TIMESTAMP(), 400, 2,
#                          current_timestamp());
#
# insert into goods values(6,2, 2,'GoodsName6','GoodsContent6',
#                          35000,'writer3', 'writerContent3', sysdate(),
#                          6000, CURRENT_TIMESTAMP(), 300, 2,
#                          current_timestamp());
#
# insert into goods values(7,1, 3,'GoodsName7','GoodsContent7',
#                          20000,'writer1', 'writerContent1',sysdate(),
#                          7000, CURRENT_TIMESTAMP(), 300, 1,
#                          current_timestamp());
# insert into goods values(8,2, 3,'GoodsName8','GoodsContent8',
#                          30000,'writer2', 'writerContent2', sysdate(),
#                          8000, CURRENT_TIMESTAMP(), 350, 2,
#                          current_timestamp());
#
# insert into goods values(9,2, 3,'GoodsName9','GoodsContent9',
#                          35000,'writer2', 'writerContent2', sysdate(),
#                          9000, CURRENT_TIMESTAMP(), 350, 2,
#                          current_timestamp());
#
# insert into goods values(10,2, 4,'GoodsName10','GoodsContent10',
#                          20000,'writer2', 'writerContent2', sysdate(),
#                          10000, CURRENT_TIMESTAMP(), 400, 2,
#                          current_timestamp());
#
# insert into goods values(11,2, 4,'GoodsName11','GoodsContent11',
#                          35000,'writer3', 'writerContent3', sysdate(),
#                          11000, CURRENT_TIMESTAMP(), 400, 2,
#                          current_timestamp());
#
# insert into goods values(12,2, 4,'GoodsName12','GoodsContent12',
#                          35000,'writer3', 'writerContent3', sysdate(),
#                          12000, CURRENT_TIMESTAMP(), 300, 2,
#                          current_timestamp());


commit;

select * from goods;

delete from goods;

SELECT
    goods_no,
    goods_name,
    goods_writer,
    goods_date
FROM goods
ORDER BY
    goods_date DESC
LIMIT 100;

update goods
set goods_name = '소년이 온다', goods_writer = '한강'
where goods_no = 500004;





