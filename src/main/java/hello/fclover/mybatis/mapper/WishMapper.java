package hello.fclover.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WishMapper {
    // 찜 생성
    int insertWish(@Param("goodsNo") Long goodsNo, @Param("memberNo") Long memberNo);

    // 찜 해제
    int deleteWish(@Param("goodsNo") Long goodsNo, @Param("memberNo") Long memberNo);

    // 찜 존재 여부 확인
    boolean isWishExists(@Param("goodsNo") Long goodsNo, @Param("memberNo") Long memberNo);
}



