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
    private Long paymentsNo;
    private int invenGoodsNo;
    private String deliType;
    private int deliQuan;
    private String deliContent;
    private LocalDateTime deliDate;
    private String deliCusName;
    private String deliStatus;

}
