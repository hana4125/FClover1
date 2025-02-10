package hello.fclover.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WishMapper {
    // 찜 생성
    int insertWish(@Param("goodsNo") Long goodsNo, @Param("memberNo") Long memberNo);

    // 찜 해제
    int deleteWish(@Param("goodsNo") Long goodsNo, @Param("memberNo") Long memberNo);

    // 찜 존재 여부 확인
    boolean isWishExists(@Param("goodsNo") Long goodsNo, @Param("memberNo") Long memberNo);

    // 특정 회원의 찜 목록에 있는 goodsNo 목록 가져오기
    List<Long> findWishlistGoodsNosByMemberNo(@Param("goodsNo") Long goodsNo, @Param("memberNo") Long memberNo);
}



