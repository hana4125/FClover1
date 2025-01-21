package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Goods;
import hello.fclover.dto.SearchDetailForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {
    int goodsInsertText(Goods goods);

    int goodsNoselect(Long sellerNo, String goodsName);
  
    List<Goods> findAll(@Param("cate_no") int cateNo,
                        @Param("sort") String sort,
                        @Param("offset") int offset,
                        @Param("size") int size);

    int countGoods(int cate_no);

    Goods findGoodsById(long goods_no);

    List<Goods> findGoodsByKeyword(@Param("keyword") String keyword);

    List<Goods> findGoodsByDetail(SearchDetailForm searchDetailForm);
}
