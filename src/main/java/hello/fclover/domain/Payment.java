package hello.fclover.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {


    private Long id;               // 결제 ID
    private Long partnerId;        // 파트너 ID (nullable)
    private Long userId;           // 사용자 ID
    private Long orderId;          // 주문 ID
    private BigDecimal paymentAmount; // 결제 금액
    private LocalDateTime paymentDate; // 결제 일자
    private String impUid;         // 결제 고유 ID (nullable)
    private String paymentMethod;  // 결제 방식 (nullable)
    private String merchantUid;    // 가맹점 고유 ID (nullable)
    private String pgProvider;     // PG 제공업체 (nullable)
    private String pgType;         // PG 유형 (nullable)
    private String pgTid;          // PG 거래 고유 ID (nullable)
    private String status;         // 결제 상태 (nullable)
    private String cardName;       // 카드 이름 (nullable)
    private String cardNumber;     // 카드 번호 (nullable)
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 업데이트일


    public static Payment save(PaymentReq paymentReq) {
        return Payment.builder()
                .partnerId(paymentReq.getPartnerId())
                .userId(paymentReq.getUserId())
                .orderId(paymentReq.getOrderId())
                .impUid(paymentReq.getImpUid())
                .paymentMethod(paymentReq.getPayMethod())
                .merchantUid(paymentReq.getMerchantUid())
                .paymentAmount(BigDecimal.valueOf(paymentReq.getPaidAmount()))
                .pgProvider(paymentReq.getPgProvider())
                .pgType(paymentReq.getPgType())
                .pgTid(paymentReq.getPgTid())
                .status(paymentReq.getStatus())
                .cardName(paymentReq.getCardName())
                .cardNumber(paymentReq.getCardNumber())
                .build();
    }
}
