package hello.fclover.domain;


import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class Goods {
    // 임시로 SQL 문 대로 생성
    private int goodsNo;        // 상품 키
    private String sellerId;           // 판매자 아이디
    private Integer cateNo;            // 카테고리 번호
    private String goodsName;          // 상품 명
    private String goodsContent;       // 상품 설명
    private int goodsPrice;            // 상품 가격
    private String goodsWriter;        // 상품 저자
    private String sellerCompany;      // 출판사
    private LocalDate goodsCreateAt;   // 상품 발행일
    private int goodsCount;            // 상품 총 판매수량
    private LocalDateTime goodsDate;   // 상품 등록일
    private int goodsPageCount;        // 상품 페이지 수
    private String goodsBookSize;      // 상품 크기
    private String writerContent;      //작가 상세 내용
    private LocalDateTime updateAt;    // 상품내용 수정일.
}