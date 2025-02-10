package hello.fclover.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    private Long deliNo;
    private Long orderId;
    private String userId;
    private String deliStatus;
    private int deliNum;
    private String deliCompany;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
