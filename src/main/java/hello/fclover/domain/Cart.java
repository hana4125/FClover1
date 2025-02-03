package hello.fclover.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Cart {
    private Long CartNo;        // 기본 키

    private Long memberNo;      // 구매자 키

    private Long goodsNo;       // 상품 키

    private int cartQuantity;         // 카트에 담긴 상품 수량

    private LocalDateTime createdAt; // 카트에 담은 날짜 (배송일에 필요)
}
