package hello.fclover.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    private Long goodsNo;              // 상품 일련번호
    private Long sellerNo;             // 판매자 번호
    private int cateNo;                // 카테고리 번호
    private String goodsName;          // 상품 명
    private String goodsContent;       // 상품 설명
    private int goodsPrice;            // 상품 가격
    private String goodsWriter;        // 상품 저자
    private String companyName;        // 출판사 이름
    private String writerContent;      // 저자 설명
    private LocalDate goodsCreateAt;   // 상품 발행일
    private int goodsCount;            // 상품 총 판매수량
    private LocalDateTime goodsDate;   // 상품 등록일
    private int goodsPageCount;        // 상품 페이지 수
    private String goodsBookSize;      // 상품 크기
    private LocalDateTime updateAt;           // 상품 수정일
    private String cateName;           // 카테고리 이름
    private String wishStatus; // 'Y' 또는 'N'
    private int rowNum; // 상품 순위
    private GoodsImage mainImage;      // 대표 이미지
    private String permission;
}

