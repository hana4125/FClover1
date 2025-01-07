package hello.fclover.domain;

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
public class Settlement {
    private Long id;
    private Long partnerId;
    private BigDecimal totalAmount;
    private String status="Pending";
    private LocalDate paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Settlement create(Long partnerId, BigDecimal totalAmount, LocalDate paymentDate) {
        Settlement settlement = new Settlement();
        settlement.setPartnerId(partnerId);
        settlement.setTotalAmount(totalAmount);
        settlement.setStatus("completed");
        settlement.setPaymentDate(paymentDate);
        settlement.setCreatedAt(LocalDateTime.now());
        settlement.setUpdatedAt(LocalDateTime.now());
        return settlement;

    }

}
