package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 모든 카테고리 조회
    List<Category> findAll();

    Category findTitle(int cateNo);
}

