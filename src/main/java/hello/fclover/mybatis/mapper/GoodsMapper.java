package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {

    List<Goods> findAll(int cate_no);

    // 특정 카테고리 조회
    Goods findById(int cate_no);
}
