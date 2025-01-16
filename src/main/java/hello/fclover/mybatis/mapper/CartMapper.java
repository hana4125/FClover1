package hello.fclover.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartMapper {
    // 이미 있으면 UPDATE, 없으면 INSERT
    int upsertCart(@Param("goodsNo") Long goodsNo,
                   @Param("memberNo") Long memberNo);
}

