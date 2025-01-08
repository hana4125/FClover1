package hello.fclover.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    private Long id;
    private int goodsNo;
    private int goodsCode;
    private int initialStock;
    private int productStock;
    private int isSoldOut;


}
