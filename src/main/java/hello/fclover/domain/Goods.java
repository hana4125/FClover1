package hello.fclover.domain;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Goods {
    // 임시로 SQL 문 대로 생성
    private BigInteger goodsNo;        // 상품 키

    private Integer goodsIsbn;         // 상품 번호

    private String sellerId;           // 판매자 아이디

    private Integer cateNo;            // 카테고리 번호

    private String goodsName;          // 상품 명

    private String goodsContent;       // 상품 설명

    private int goodsPrice;            // 상품 가격

    private String goodsWriter;        // 상품 저자

    private String sellerCompany;      // 출판사

    private LocalDate goodsCreateAt;   // 상품 발행일

    private String goodsImage;         // 상품 이미지 링크

    private int goodsCount;            // 상품 총 판매수량

    private LocalDateTime goodsDate;   // 상품 등록일
}
