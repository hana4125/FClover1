package hello.fclover.service;

import hello.fclover.domain.Category;
import hello.fclover.mybatis.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = "CategoryMapper.findAll")
    public List<Category> getCategoryList() {
        // 모든 카테고리 가져오기
        long countStartTime = System.currentTimeMillis();
        List<Category> result = categoryMapper.findAll();
        long countEndTime = System.currentTimeMillis();
        long totalCountTime = countEndTime - countStartTime;
        log.info("카테고리 가져오기 소요 시간: {} ms", totalCountTime);
        return result;

    }

    @Override
    public Category getCategoryByNo(int cateNo) {
        return categoryMapper.findTitle(cateNo);
    }
}
