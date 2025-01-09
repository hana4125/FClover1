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

    // 특정 카테고리 조회
    Goods findById(int cate_no);
}
