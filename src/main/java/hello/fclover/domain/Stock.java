package hello.fclover.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
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
