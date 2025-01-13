package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {

    List<Goods> findAll(@Param("cate_no") int cateNo,
                        @Param("sort") String sort,
                        @Param("offset") int offset,
                        @Param("size") int size);

    int countGoods(int cate_no);

    Goods findGoodsById(long goods_no);
}
