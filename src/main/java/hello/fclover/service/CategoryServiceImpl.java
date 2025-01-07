package hello.fclover.service;

import hello.fclover.domain.Category;
import hello.fclover.mybatis.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategoryList() {
        // 모든 카테고리 가져오기
        return categoryMapper.findAll();
    }

    @Override
    public Category getCategoryById(int cate_no) {
        // 카테고리 ID로 특정 카테고리 가져오기
        return categoryMapper.findById(cate_no);
    }
}
