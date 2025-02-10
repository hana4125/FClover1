package hello.fclover.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Results;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {


    private Long paymentsNo;// 결제 ID
    private Long sellerNo;        // 파트너 ID (nullable)
    private String userId;           // 사용자 ID
    private Long orderId;// 주문 ID
    private int quantity; //주문 수량
    private Long goodsNo; //상품번호
    private BigDecimal paymentAmount; // 결제 금액
    private LocalDate paymentDate; // 결제 일자
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
                .sellerNo(paymentReq.getPartnerId())
                .userId(paymentReq.getUserId())
                .orderId(paymentReq.getOrderId())
                .impUid(paymentReq.getImpUid())
                .quantity(paymentReq.getQuantity())
                .goodsNo(paymentReq.getGoodsNo())
                .paymentDate(paymentReq.getPaymentDate())
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
