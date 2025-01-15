package hello.fclover.mybatis.mapper;


import hello.fclover.domain.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper {

    int goodsInsertText(Goods goods);

    int goodsNoselect(String sellerId, String goodsName);
}
