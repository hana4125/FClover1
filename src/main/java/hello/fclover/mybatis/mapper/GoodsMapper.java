package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Goods;
import hello.fclover.dto.SearchDetailForm;
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

    Goods findGoodsById(long goods_no);

    int countGoodsByKeyword(String keyword);

    List<Goods> findGoodsByKeyword(Map<String, Object> params);

    List<Goods> findGoodsByDetail(SearchDetailForm searchDetailForm);
}
