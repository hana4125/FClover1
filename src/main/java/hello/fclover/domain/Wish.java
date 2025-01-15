package hello.fclover.domain;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Wish {

    private BigInteger wishNo;        // 기본 키

    private BigInteger memberNo;      // 구매자 키

    private BigInteger goodsNo;
}
