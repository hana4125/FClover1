package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SellerMapper {

    void goodsSingleInsert(Goods goods);
}
