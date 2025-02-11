package hello.fclover.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessGoods {
    private Long goodsNo;              // 상품 번호
    private int cateNo;                // 카테고리 번호
    private String goodsName;          // 상품 명
    private String goodsContent;       // 상품 설명
    private int goodsPrice;            // 상품 가격
    private String goodsWriter;        // 상품 저자
    private String writerContent;      // 저자 설명
    private String goodsCreateAt;   // 상품 발행일
    private int goodsPageCount;        // 상품 페이지 수
    private String goodsBookSize;      // 상품 크기
    private Long sellerNo;             // 판매자 번호
    private String imageUrl;            //상품 이미지 url
    private String mainImage;          // 대표이미지
    private String subImage1;           //추가이미지1
    private String subImage2;           //추가이미지2
    private String cateName;
    private String sellerName;
}
