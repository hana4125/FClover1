package hello.fclover.mybatis.mapper;

import hello.fclover.domain.GoodsImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsImageMapper {
    void goodsInsertImage(GoodsImage goodsImage);

    List<Map<String, String>> findAllGoodsImage(Long goodsNo);

    // findMainImageByGoodsNo 메서드 추가
    GoodsImage findMainImageByGoodsNo(@Param("goodsNo") Long goodsNo);
}
