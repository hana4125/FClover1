package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Goods;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {
    int goodsInsertText(Goods goods);

    int goodsNoselect(String sellerNo, String goodsName);
  
    List<Goods> findAll(@Param("cate_no") int cateNo,
                        @Param("sort") String sort,
                        @Param("offset") int offset,
                        @Param("size") int size);

    int countGoods(int cate_no);

    Goods findGoodsById(long goodsNo);

    int countGoodsByKeyword(String keyword);

    List<Goods> findGoodsByKeyword(Map<String, Object> params);

    int countGoodsByParam(Map<String, Object> params);

    List<Goods> findGoodsByParam(Map<String, Object> params);

    long countAll();

    void insertGoods(@Param("goods") List<Goods> goods);

    // 인덱스 활성화, 비활성화용
    // void dropFullTextIndex();
    // void createFullTextIndex();
}
