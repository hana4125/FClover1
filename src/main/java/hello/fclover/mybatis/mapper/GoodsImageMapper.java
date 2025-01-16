package hello.fclover.mybatis.mapper;

import hello.fclover.domain.GoodsImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsImageMapper {
    void goodsInsertImage(GoodsImage goodsImage);
}
