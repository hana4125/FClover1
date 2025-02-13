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
public class PaymentDeliveryDTO {
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

    private Long deliNo;
    private int invenGoodsNo;
    private String deliType;
    private int deliQuan;
    private String deliContent;
    private LocalDateTime deliDate;
    private String deliCusName;
    private String deliStatus;
}
