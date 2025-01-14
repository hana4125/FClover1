package hello.fclover.domain;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class Cart {
    private BigInteger CartGoodsNo;   // 기본 키

    private String memberId;             // 구매자 아이디

    private int cartId;               // 카드 아이디

    private BigInteger goodsNo;       // 카테고리 번호

    private int cartQuan;             // 카트에 담긴 상품 수량

    private LocalDateTime added_at;   // 추가한 시간
}
