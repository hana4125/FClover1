package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import hello.fclover.domain.MessGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessGoodsMapper {
    void saveProduct(Goods goods);

    void saveProductImage(GoodsImage goodsImage);
}
