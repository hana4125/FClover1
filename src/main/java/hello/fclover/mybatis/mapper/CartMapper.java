package hello.fclover.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartMapper {
    // 장바구니에 상품을 새로 추가 (수량은 1로 고정한다고 가정)
    int insertCart(@Param("goodsNo") Long goodsNo, @Param("memberNo") Long memberNo);

    // 이미 담긴 상품이면 수량 +1
    int updateCartQuantity(@Param("goodsNo") Long goodsNo, @Param("memberNo") Long memberNo);

    // 장바구니 존재 여부 확인
    boolean isCartExists(@Param("goodsNo") Long goodsNo, @Param("memberNo") Long memberNo);
}
