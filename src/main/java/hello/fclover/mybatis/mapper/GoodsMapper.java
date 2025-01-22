package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Goods;
import hello.fclover.dto.SearchDetailForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {
    int goodsInsertText(Goods goods);

    int goodsNoselect(String sellerNo, String goodsName);
  
//    List<Goods> findAll(@Param("memberNo") Long memberNo,
//                        @Param("cate_no") int cateNo,
//                        @Param("sort") String sort,
//                        @Param("offset") int offset,
//                        @Param("size") int size);

    int countGoods(int cate_no);

    int countBestGoods();

    Goods findGoodsById(long goodsNo);

    List<Goods> findGoodsByKeyword(@Param("keyword") String keyword);

    List<Goods> findGoodsByDetail(SearchDetailForm searchDetailForm);

    // 찜 상태를 포함한 상품 조회 메서드 추가
    List<Goods> findGoodsWithWishStatus(
            @Param("memberNo") Long memberNo,
            @Param("cateNo") Integer cateNo,
            @Param("sort") String sort,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );

    List<Goods> findGoodsWishStatus(
            @Param("memberNo") Long memberNo,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );

    List<Goods> findByRank();
}
