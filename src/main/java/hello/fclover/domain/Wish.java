package hello.fclover.domain;

import lombok.Data;

@Data
public class Wish {

    private Long wishNo;        // 기본 키

    private Long memberNo;      // 구매자 키

    private Long goodsNo;       // 상품 키
}
