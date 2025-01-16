package hello.fclover.domain;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Cart {
    private BigInteger CartNo;        // 기본 키

    private BigInteger memberNo;      // 구매자 키

    private BigInteger goodsNo;       // 상품 키

    private int cartQuantity;         // 카트에 담긴 상품 수량
}
