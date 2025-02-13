package hello.fclover.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentGoodsImageDTO {
    // Payment 관련 필드
    private Long paymentsNo; // 결제 ID
    private Long partnerId; // 파트너 ID
    private String userId; // 사용자 ID
    private Long orderId; // 주문 ID
    private int quantity; // 주문 수량
    private Long goodsNo; // 상품 번호
    private BigDecimal paymentAmount; // 결제 금액
    private LocalDate paymentDate; // 결제 일자
    private String impUid; // 결제 고유 ID
    private String paymentMethod; // 결제 방식
    private String merchantUid; // 가맹점 고유 ID
    private String pgProvider; // PG 제공업체
    private String pgType; // PG 유형
    private String pgTid; // PG 거래 고유 ID
    private String status; // 결제 상태
    private String cardName; // 카드 이름
    private String cardNumber; // 카드 번호
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 업데이트일

    // Goods 관련 필드
    private Long sellerNo; // 판매자 번호
    private int cateNo; // 카테고리 번호
    private String goodsName; // 상품 명
    private String goodsContent; // 상품 설명
    private int goodsPrice; // 상품 가격
    private String goodsWriter; // 상품 저자
//    private String companyName; // 출판사 이름
    private String writerContent; // 저자 설명
    private LocalDate goodsCreateAt; // 상품 발행일
    private LocalDateTime goodsDate; // 상품 등록일
    private int goodsPageCount; // 상품 페이지 수
    private String goodsBookSize; // 상품 크기
    private LocalDateTime updateAt; // 상품 수정일
//    private String cateName; // 카테고리 이름
//    private String wishStatus; // 'Y' 또는 'N'
//    private int rowNum; // 상품 순위
//    private GoodsImage mainImage; // 대표 이미지
    private String permission; // 권한


    private Long imageNo; // 이미지 키
    private String goodsImageName; // 이미지 이름
    private String goodsUrl; //이미지 저장 path
    private String isMain; //대표이미지 설정
}
