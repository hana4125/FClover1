DROP TABLE GOODS;

CREATE TABLE GOODS
(
    GOODS_NO            BIGINT AUTO_INCREMENT PRIMARY KEY, #--상품 일련번호
    SELLER_NO           BIGINT NOT NULL, #--판매자 번호
    CATE_NO             INT, #--카테고리 번호
    GOODS_NAME          VARCHAR(30) not null, #--상품명
    GOODS_CONTENT       VARCHAR(100), #--상품 설명
    GOODS_PRICE         INT NOT NULL, #--상품 가격
    GOODS_WRITER        VARCHAR(20) not null, #--상품 저자
    WRITER_CONTENT      VARCHAR(100), #--저자 설명
    GOODS_CREATE_AT     DATE NOT NULL, #--상품 발행일
    GOODS_COUNT         INT DEFAULT 0, #--상품 총 판매수량 (베스트/스테디셀러 확인위해 필요한 값)
    GOODS_DATE          TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP, #--상품 등록일
    GOODS_PAGECOUNT     INT, #--상품 페이지수
    GOODS_BOOKSIZE      VARCHAR(10), #--상품 크기
    UPDATE_AT           DATE, #--수정 일자
    FOREIGN KEY (SELLER_NO) REFERENCES SELLER(SELLER_NO) ON DELETE CASCADE,
    FOREIGN KEY (CATE_NO) REFERENCES CATEGORY(CATE_NO) ON UPDATE CASCADE,
    FULLTEXT KEY ft_idx_goods (GOODS_NAME, GOODS_CONTENT, GOODS_WRITER) WITH PARSER ngram
) ENGINE=InnoDB;

commit;

select * from goods;

delete from goods where goods_no=1;

