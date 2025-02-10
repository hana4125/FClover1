package hello.fclover.service;

import hello.fclover.domain.Category;
import hello.fclover.dto.CategoryCountDTO;
import hello.fclover.mybatis.mapper.CategoryMapper;
import hello.fclover.mybatis.mapper.GoodsMapper;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountServiceImpl implements CountService {

    private final GoodsMapper goodsMapper;
    private final CategoryMapper categoryMapper;

    @Override
    @Cacheable(cacheNames = "keywordCountCache", key = "#keyword")
    public int countByKeyword(String keyword) {
        log.info("DB에서 countByKeyword 호출: {}", keyword);
        return goodsMapper.countGoodsByKeyword(keyword);
    }

    @Override
    @Cacheable(cacheNames = "categoryCountCache", key = "#keyword")
    public Map<Category, Integer> getCategoryCount(String keyword) {
        long start = System.currentTimeMillis();

        List<CategoryCountDTO> countCategories = goodsMapper.countCategoryByKeyword(keyword);
        Map<Category, Integer> categoryList = new HashMap<>();
        for (CategoryCountDTO countCategory : countCategories) {
            int cateNo = countCategory.getCateNo();
            Category category = categoryMapper.findTitle(cateNo);
            categoryList.put(category, countCategory.getCount());
        }

        Map<Category, Integer> sortedCategoryMap = categoryList.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Entry::getKey,
                        Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        long elapsed = System.currentTimeMillis() - start;
        log.info("[{}] getCategoryCount 실행 시간: {} ms", Thread.currentThread().getName(), elapsed);
        return sortedCategoryMap;
    }
}
