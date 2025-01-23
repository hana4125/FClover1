package hello.fclover.domain;

import lombok.Data;

@Data
public class Cart {
    private Long CartNo;        // 기본 키

    private Long memberNo;      // 구매자 키

    private Long goodsNo;       // 상품 키

    private int cartQuantity;         // 카트에 담긴 상품 수량
}
