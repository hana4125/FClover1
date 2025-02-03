package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Goods;
import hello.fclover.dto.SearchParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsMapper {
    int goodsInsertText(Goods goods);

    Long goodsNoselect(Long sellerNo, String goodsName);
  
//    List<Goods> findAll(@Param("memberNo") Long memberNo,
//                        @Param("cate_no") int cateNo,
//                        @Param("sort") String sort,
//                        @Param("offset") int offset,
//                        @Param("size") int size);

    int countGoods(int cate_no);

    int countBestGoods();

    Goods findGoodsById(long goodsNo);

    int countGoodsByKeyword(String keyword);

//    List<Goods> findGoodsByDetail(SearchDetailForm searchDetailForm);

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

    List<Goods> findByRank(
            @Param("limit") int limit
    );
  
    List<Goods> findGoodsByKeyword(Map<String, Object> params);

    int countGoodsByParam(Map<String, Object> params);

    List<Goods> findGoodsByParam(Map<String, Object> params);

    long countAll();

    void insertGoods(@Param("goods") List<Goods> goods);

    // 인덱스 활성화, 비활성화용
    // void dropFullTextIndex();
    // void createFullTextIndex();

    /**
     * 신상품 목록 조회
     */
    List<Goods> selectNewItems(@Param("memberNo") Long memberNo,
                               @Param("year") String year,
                               @Param("month") String month,
                               @Param("week") String week,
                               @Param("offset") int offset,
                               @Param("size") int size);

    /**
     * 신상품 총 개수 조회
     */
    int countNewItems(@Param("memberNo") Long memberNo,
                      @Param("year") String year,
                      @Param("month") String month,
                      @Param("week") String week);


    List<Goods> searchByParam(SearchParamDTO searchParamDTO, int offset);

}
